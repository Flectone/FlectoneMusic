document.querySelector('video').addEventListener('ended', e => {
    window.media.playNext(true)
});