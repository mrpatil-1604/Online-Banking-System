document.getElementById('transferForm').addEventListener('submit', function(event) {
    const fromAccount = document.getElementById('fromAccount').value;
    const toAccount = document.getElementById('toAccount').value;
    const amount = document.getElementById('amount').value;

    if (!fromAccount) {
        alert('Please select a valid "From Account".');
        event.preventDefault();
    }
    if (toAccount === '' || toAccount <= 0) {
        alert('Please enter a valid "To Account" number.');
        event.preventDefault();
    }
    if (amount === '' || amount <= 0) {
        alert('Please enter a valid amount.');
        event.preventDefault();
    }
});
