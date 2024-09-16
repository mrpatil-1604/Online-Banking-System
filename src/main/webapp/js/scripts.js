/**
 * 
 */
 // scripts.js

function validateLoginForm() {
    var email = document.forms["loginForm"]["email"].value;
    var password = document.forms["loginForm"]["password"].value;
    if (email == "" || password == "") {
        alert("Both email and password must be filled out");
        return false;
    }
    return true;
}

function validateRegisterForm() {
    var name = document.forms["registerForm"]["name"].value;
    var email = document.forms["registerForm"]["email"].value;
    var phone = document.forms["registerForm"]["phone"].value;
    var password = document.forms["registerForm"]["password"].value;
    if (name == "" || email == "" || phone == "" || password == "") {
        alert("All fields must be filled out");
        return false;
    }
    return true;
}

document.addEventListener('DOMContentLoaded', function () {
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');

    // Validate Login Form
    if (loginForm) {
        loginForm.addEventListener('submit', function (event) {
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

            if (!validateEmail(email)) {
                alert('Please enter a valid email address.');
                event.preventDefault();
            }

            if (password.length < 6) {
                alert('Password must be at least 6 characters long.');
                event.preventDefault();
            }
        });
    }

    // Validate Register Form
    if (registerForm) {
        registerForm.addEventListener('submit', function (event) {
            const name = document.getElementById('name').value;
            const email = document.getElementById('email').value;
            const phone = document.getElementById('phone').value;
            const address = document.getElementById('address').value;
            const password = document.getElementById('password').value;

            if (name === '') {
                alert('Please enter your name.');
                event.preventDefault();
            }

            if (!validateEmail(email)) {
                alert('Please enter a valid email address.');
                event.preventDefault();
            }

            if (!validatePhone(phone)) {
                alert('Please enter a valid phone number (10 digits).');
                event.preventDefault();
            }

            if (address === '') {
                alert('Please enter your address.');
                event.preventDefault();
            }

            if (password.length < 6) {
                alert('Password must be at least 6 characters long.');
                event.preventDefault();
            }
        });
    }

    // Validate Email Format
    function validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(String(email).toLowerCase());
    }

    // Validate Phone Number (10 digits)
    function validatePhone(phone) {
        const re = /^\d{10}$/;
        return re.test(String(phone));
    }
});

 