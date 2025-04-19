// Función para cargar las traducciones desde el API
async function loadTranslations() {
    try {
        // Espera hasta que las traducciones estén listas
        const response = await fetch('/api/i18n');
        const translations = await response.json();

        // Itera sobre todos los elementos con el atributo data-i18n
        document.querySelectorAll('[data-i18n]').forEach(element => {
            const key = element.getAttribute('data-i18n');
            if (translations[key]) {
                element.textContent = translations[key];  // Aplica la traducción
            }
        });
    } catch (error) {
        console.error('Error al cargar las traducciones:', error);
    }
}

// Llama a la función para cargar las traducciones cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', loadTranslations);
