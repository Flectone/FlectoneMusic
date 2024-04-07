externalAPI.on(externalAPI.EVENT_STATE, () => {

    const currentTrack = externalAPI.getCurrentTrack();

    setTimeout(() => {
            if (externalAPI.getProgress().position === 0.0 && currentTrack === externalAPI.getCurrentTrack()) {
                externalAPI.next();
            }
        }, 3000)
    }
);