document.addEventListener('DOMContentLoaded', function() {
    const colorCodeEl = document.getElementById('color-code');
    const saludoEl = document.getElementById('saludo');
    const copyBtn = document.getElementById('copy-button');
    const nombreInput = document.getElementById('nombre-input');
    const saludarBtn = document.getElementById('saludar-button');

    function actualizarColor(color) {
        document.body.style.backgroundColor = color;
        colorCodeEl.textContent = color;
    }

    // Asegurar sincronización inicial del color de fondo
    const initialColor = colorCodeEl.textContent.trim();
    if (initialColor) {
        actualizarColor(initialColor);
    }

    // Copiar con fallback
    async function copiarTexto(texto) {
        try {
            await navigator.clipboard.writeText(texto);
            return true;
        } catch (err) {
            const temp = document.createElement('input');
            temp.value = texto;
            document.body.appendChild(temp);
            temp.select();
            temp.setSelectionRange(0, 99999);
            try {
                const success = document.execCommand('copy');
                document.body.removeChild(temp);
                return success;
            } catch (e) {
                document.body.removeChild(temp);
                return false;
            }
        }
    }

    copyBtn.addEventListener('click', async function() {
        const color = colorCodeEl.textContent;
        const ok = await copiarTexto(color);
        if (ok) {
            copyBtn.textContent = '¡Copiado!';
            setTimeout(() => { copyBtn.textContent = 'Copiar Hex'; }, 2000);
        } else {
            alert('No se pudo copiar el código');
        }
    });

    saludarBtn.addEventListener('click', async function() {
        const nombre = nombreInput.value.trim();
        if (nombre === '') {
            alert('Por favor, escribe un nombre');
            return;
        }

		try {
            const response = await fetch('/saludar', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ nombre: nombre })
                //body: formData
            });


            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.detail || 'Error al saludar');
            }

            const data = await response.json();
            saludoEl.textContent = data.saludo;
            actualizarColor(data.color);
            nombreInput.value = '';
            // Recargar el historial tras registrar un nuevo saludo
            cargarHistorial();
        } catch (error) {
            alert('Hubo un error: ' + error.message);
        }
    });

    nombreInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            saludarBtn.click();
        }
    });

    // Cargar historial desde PostgreSQL (GET /historial)
    const historialList = document.getElementById('historial-list');

    async function cargarHistorial() {
        try {
            const res = await fetch('/historial');
            if (!res.ok) return;
            const items = await res.json();
            historialList.innerHTML = '';
            items.forEach(item => {
                const li = document.createElement('li');
                li.innerHTML = `
                    <span>
                        <span class="color-badge" style="background-color: ${item.color};"></span>
                        <strong>${item.nombre}</strong>: ${item.mensaje}
                    </span>
                    <small style="opacity: 0.7;">${new Date(item.fechaHora).toLocaleTimeString()}</small>
                `;
                historialList.appendChild(li);
            });
        } catch (err) {
            console.error('Error cargando historial:', err);
        }
    }

    cargarHistorial();
});