Toast = {
    success: function (message) {
        Swal.fire({
            position: 'center',
            icon: 'success',
            title: message,
            showConfirmButton: true,
            timer: 3000
        })
    },
    error: function (message) {
        Swal.fire({
            position: 'center',
            icon: 'error',
            title: message,
            showConfirmButton: true,
            timer: 3000
        })
    },
    warning: function (message) {
        Swal.fire({
            position: 'center',
            icon: 'warning',
            title: message,
            showConfirmButton: true,
            timer: 3000
        })
    }
};