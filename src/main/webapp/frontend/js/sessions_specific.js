
function playPreview(uri) {
    var sound = new Howl({
        src: [uri]
    });

    sound.play();
}

// TODO: Finish this