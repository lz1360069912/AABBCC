Confirm = {
    show: function (title,text,callback) {
        Swal.fire({
            title: title,
            text: text,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            cancelButtonText: '取消',
            confirmButtonText: '确认'
        }).then((result) => {
                if (result.isConfirmed) {
                    if (callback) {
                        callback();
                    }
                }
            }
        )
    }
}