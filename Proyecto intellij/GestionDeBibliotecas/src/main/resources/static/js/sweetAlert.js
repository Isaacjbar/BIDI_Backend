function showAlert(icon, title, text, redirectUrl) {
    const container = document.getElementById('container');
    const originalPosition = container.scrollTop;

    container.style.overflow = 'hidden';

    Swal.fire({
        icon: icon,
        title: title,
        text: text,
        showConfirmButton: false,
        stopKeydownPropagation: true,
        focusConfirm: false,
        scrollbarPadding: false,
        timer: 4500,
        customClass: {
            popup: 'my-alert-popup',
        }
    }).then(() => {
        container.scrollTop = originalPosition;

        if (redirectUrl !== "") {
            setTimeout(() => {
                window.location.href = redirectUrl;
            }, 4500);
        }
    });
}