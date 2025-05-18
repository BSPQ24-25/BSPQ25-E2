let i18nTranslations = {};
window.t = function(key) {
  return i18nTranslations[key] || key;
};

if (!localStorage.getItem("token") && localStorage.getItem("jwtToken")) {
    const oldToken = localStorage.getItem("jwtToken");
    localStorage.setItem("token", oldToken);
    localStorage.removeItem("jwtToken");

    const base64Url = oldToken.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(c =>
        '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
    ).join(''));

    const payload = JSON.parse(jsonPayload);
    localStorage.setItem("username", payload.sub);
}

async function loadTranslations(lang = null) {
    try {
        const selectedLang = lang || localStorage.getItem('lang') || 'es';
        localStorage.setItem('lang', selectedLang);

        const response = await fetch(`/api/i18n?lang=${selectedLang}`);
        const translations = await response.json();

        i18nTranslations = translations;

        document.querySelectorAll('[data-i18n]').forEach(el => {
            const key = el.getAttribute('data-i18n');
            if (translations[key]) {
                el.textContent = translations[key];
            }
        });

        document.querySelectorAll('[data-i18n-placeholder]').forEach(el => {
            const key = el.getAttribute('data-i18n-placeholder');
            if (translations[key]) {
                el.setAttribute('placeholder', translations[key]);
            }
        });

    } catch (error) {
        console.error('Error al cargar las traducciones:', error);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    loadTranslations();

    const langSelector = document.getElementById('languageSelector');
    if (langSelector) {
        langSelector.addEventListener('change', e => {
            loadTranslations(e.target.value);
        });
    }

    document.querySelectorAll('[data-lang-btn]').forEach(button => {
        button.addEventListener('click', () => {
            loadTranslations(button.getAttribute('data-lang-btn'));
        });
    });
});
