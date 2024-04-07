externalAPI.on(externalAPI.EVENT_STATE, () => {
    setTimeout(() => {
            if (externalAPI.getProgress().position === 0.0 ) {
                externalAPI.togglePause();
            }
        }, 3000)
    }
);