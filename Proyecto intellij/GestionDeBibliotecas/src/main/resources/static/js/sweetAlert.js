function showAlert(icon, title, text, redirectUrl) {
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
        if (redirectUrl !== "") {
            setTimeout(() => {
                window.location.href = redirectUrl;
            }, 4500);
        }
    });
}