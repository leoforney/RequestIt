var sound;
function playPreview(uri) {
    if (sound !== undefined) {
        sound.pause();
    }
    if (uri !== undefined) {
        sound = new Howl({
            src: [uri],
            html5: true,
            format: ['mp3'],
            volume: 0.4
        });

        sound.play();
    }
}