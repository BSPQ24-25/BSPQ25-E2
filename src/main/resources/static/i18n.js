async function loadTranslations(lang = null) {
    try {
        // Usa el idioma proporcionado o el que esté guardado, por defecto 'es'
        const selectedLang = lang || localStorage.getItem('lang') || 'es';
        localStorage.setItem('lang', selectedLang);

        // Establece el selector de idioma si existe
        const langSelector = document.getElementById('languageSelector');
        if (langSelector) {
            langSelector.value = selectedLang;
        }

        // Llama al backend con el idioma correcto
        const response = await fetch(`/api/i18n?lang=${selectedLang}`);
        const translations = await response.json();

        // Traducción de elementos con texto
        document.querySelectorAll('[data-i18n]').forEach(element => {
            const key = element.getAttribute('data-i18n');
            if (translations[key]) {
                element.textContent = translations[key];
            }
        });

        // Traducción de placeholders
        document.querySelectorAll('[data-i18n-placeholder]').forEach(element => {
            const key = element.getAttribute('data-i18n-placeholder');
            if (translations[key]) {
                element.setAttribute('placeholder', translations[key]);
            }
        });

    } catch (error) {
        console.error('Error al cargar las traducciones:', error);
    }
}

// Ejecuta al cargar el DOM y escucha cambios de idioma si hay selector
document.addEventListener('DOMContentLoaded', () => {
    loadTranslations();

    const langSelector = document.getElementById('languageSelector');
    if (langSelector) {
        langSelector.addEventListener('change', (e) => {
            loadTranslations(e.target.value);
        });
    }

    // Si usas botones de bandera (🇪🇸, 🇬🇧...), activa su funcionalidad
    document.querySelectorAll('[data-lang-btn]').forEach(button => {
        button.addEventListener('click', () => {
            const lang = button.getAttribute('data-lang-btn');
            loadTranslations(lang);
        });
    });
});
