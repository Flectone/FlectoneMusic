if (typeof externalAPI !== 'undefined') {
    if (!externalAPI.isPlaying()) {
        externalAPI.togglePause();
    } else {
        externalAPI.next();
    }
}