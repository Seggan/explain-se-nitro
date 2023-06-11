window.onload = function() {
    const params = new URLSearchParams(window.location.search);
    const part = parseInt(params.get('p')) || 1;
    const chapter = parseInt(params.get('c')) || 1;

    fetch(`./data/p${part}c${chapter}.html`).then(response => response.text()).then(data => {
        document.querySelector("#content").innerHTML = data;
    });

    document.querySelector("#prev-s").onclick = function() {
        window.location.href = `?p=${part - 1}&c=${chapter}`;
    }
    document.querySelector("#next-s").onclick = function() {
        window.location.href = `?p=${part + 1}&c=${chapter}`;
    }
    document.querySelector("#prev-c").onclick = function() {
        window.location.href = `?p=${part}&c=${chapter - 1}`;
    }
    document.querySelector("#next-c").onclick = function() {
        window.location.href = `?p=${part}&c=${chapter + 1}`;
    }
    document.querySelector("#section").innerHTML = `Part ${part}`;
    document.querySelector("#chapter").innerHTML = `Chapter ${chapter}`;
}