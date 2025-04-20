// Función para cargar las traducciones desde el API
async function loadTranslations() {
    try {
        const response = await fetch('/api/i18n');
        const translations = await response.json();

        // Traducción de elementos con texto (data-i18n)
        document.querySelectorAll('[data-i18n]').forEach(element => {
            const key = element.getAttribute('data-i18n');
            if (translations[key]) {
                element.textContent = translations[key];
            }
        });

        // Traducción de placeholders (data-i18n-placeholder)
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

// Ejecuta al cargar el DOM
document.addEventListener('DOMContentLoaded', loadTranslations);
