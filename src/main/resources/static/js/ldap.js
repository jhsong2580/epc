function logout() {

    $.ajax({
        url: "/logout",
        method: "POST",
        contentType: "application/json",
        success: function (xhr) {
            window.location.replace("/login")

        },
        error: function (xhf) {
            console.log("error")
        }
    })
}
