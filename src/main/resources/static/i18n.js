// Función para cargar las traducciones
function loadTranslations() {
    fetch('/api/i18n')
        .then(response => response.json())
        .then(translations => {
            // Itera sobre todos los elementos con el atributo data-i18n
            document.querySelectorAll('[data-i18n]').forEach(element => {
                const key = element.getAttribute('data-i18n');
                if (translations[key]) {
                    element.textContent = translations[key]; // Aplica la traducción
                }
            });
        })
        .catch(error => {
            console.error('Error al cargar las traducciones:', error);
        });
}

// Llama a la función para cargar las traducciones al cargar la página
document.addEventListener('DOMContentLoaded', loadTranslations);
