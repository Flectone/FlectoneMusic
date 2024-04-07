setTimeout(() => {
    if (document.querySelector('.ytp-time-current').textContent === "0:00") {
        document.querySelector('.ytp-cued-thumbnail-overlay').click();
    }
}, 2000)