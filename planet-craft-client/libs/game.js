var camera, scene, renderer, stats;
var planet;
var gui;
var rayCaster = new THREE.Raycaster();
var mouse = new THREE.Vector2();

var ws = (function () {
    // 连接服务器
    var ws = new WebSocket("ws://localhost:8080/planet/craft/socket");

    //连接发生错误的回调方法
    ws.onerror = function () {

    };

    //连接成功建立的回调方法
    ws.onopen = function (event) {
    };

    //接收到消息的回调方法
    ws.onmessage = function (event) {

        console.log(event);

        var packet = JSON.parse(event.data);

        // INTI Packet
        if (packet.id === 0) {
            // 初始化
            initGui();
            init(packet.data);
            animate();

            // INTI_ACK Packet
            ws.send(JSON.stringify({id: 1, data: {}}));
        } else if (packet.id === 3) {
            // CHANGE_ACK Packet
            packet.data.faces.forEach(function (face) {
                planet.geometry.faces[face].color.set(packet.data.color);
            });
            planet.geometry.colorsNeedUpdate = true;
        }

    };

    //连接关闭的回调方法
    ws.onclose = function () {

    };

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        ws.close();
    };

    return ws;
})();


var controls = new function () {
    this.color = 0x00FF00;
    this.pencil = '0';
    this.y = 10;
    this.x = 0;
    this.z = 0;
    this.rotateX = 0;
    this.rotateY = 0;
    this.rotateZ = 0;
    this.resetCamera = function () {
        this.x = 0;
        this.y = 10;
        this.z = 0;
        camera.position.set(controls.x, controls.y, controls.z);
    };
    this.resetPlanet = function () {
        this.rotateX = 0;
        this.rotateY = 0;
        this.rotateZ = 0;
        planet.rotation.x = 0;
        planet.rotation.y = 0;
        planet.rotation.z = 0;
    }
};

function initGui() {
    gui = new dat.GUI();
    var canvas = gui.addFolder("canvas");
    canvas.addColor(controls, 'color');
    canvas.add(controls, 'pencil', {square: '0', triangle: '1'});
    var position = gui.addFolder("camera");
    position.add(controls, 'x', -8, 8).step(0.01).onChange(function (e) {
        camera.position.set(controls.x, controls.y, controls.z);
    });
    position.add(controls, 'y', 2, 10).step(0.01).onChange(function (e) {
        camera.position.set(controls.x, controls.y, controls.z);
    });
    position.add(controls, 'z', -4, 4).step(0.01).onChange(function (e) {
        camera.position.set(controls.x, controls.y, controls.z);
    });
    position.add(controls, 'resetCamera').name("reset");
    var rotate = gui.addFolder("planet");
    rotate.add(controls, 'rotateX', -Math.PI / 4, Math.PI / 4).step(0.01).onChange(function (e) {
        planet.rotation.x = e;
    });
    rotate.add(controls, 'rotateY', -Math.PI / 4, Math.PI / 4).step(0.01).onChange(function (e) {
        planet.rotation.y = e;
    });
    rotate.add(controls, 'rotateZ', -Math.PI / 4, Math.PI / 4).step(0.01).onChange(function (e) {
        planet.rotation.z = e;
    });
    rotate.add(controls, 'resetPlanet').name("reset");
}

function init(data) {
    // 相机
    camera = new THREE.PerspectiveCamera(60, window.innerWidth / window.innerHeight, 1, 1000);
    camera.position.set(0, controls.y, 0);
    camera.lookAt(new THREE.Vector3(0, 0, 0));

    // 场景
    scene = new THREE.Scene();

    // 辅助线
    // scene.add(new THREE.AxesHelper(20));

    var light = new THREE.PointLight(0xffffff, 1);
    light.position.set(0, 50, 0);
    scene.add(light);

    // 平面几何体
    var geometry = new THREE.PlaneGeometry(20, 10, 512, 256);
    // 使用数据初始化平面
    for (var i = 0; i < data.length; i++) {
        geometry.faces[i].color.set(data[i]);
    }

    geometry.rotateX(-Math.PI / 2);

    var material = new THREE.MeshLambertMaterial({
        vertexColors: THREE.FaceColors,
        // wireframe: true,
        // side: THREE.DoubleSide
    });
    planet = new THREE.Mesh(geometry, material);
    scene.add(planet);

    renderer = new THREE.WebGLRenderer();
    renderer.setClearColor(0x000020);
    renderer.setPixelRatio(window.devicePixelRatio);
    renderer.setSize(window.innerWidth, window.innerHeight);

    var container = document.getElementById('container');
    container.appendChild(renderer.domElement);

    stats = new Stats();
    container.appendChild(stats.dom);

    // 在容器上注册事件，这里container也可以换成document
    container.addEventListener('click', onMouseDown, false);

    window.addEventListener('resize', onWindowResize, false);

}

function onWindowResize() {

    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();

    renderer.setSize(window.innerWidth, window.innerHeight);

}

function onMouseDown(event) {

    mouse.x = ( event.clientX / renderer.domElement.clientWidth ) * 2 - 1;
    mouse.y = -( event.clientY / renderer.domElement.clientHeight ) * 2 + 1;

    rayCaster.setFromCamera(mouse, camera);

    var intersects = rayCaster.intersectObjects(scene.children);
    if (intersects.length > 0 && intersects[0].object.id === planet.id) {
        // CHANGE Packet
        ws.send(JSON.stringify({
            id: 2,
            data: {
                face: intersects[0].faceIndex,
                color: controls.color,
                pencil: controls.pencil
            }
        }));
    }
}

function animate() {
    requestAnimationFrame(animate);
    stats.update();
    render();

}

function render() {
    renderer.render(scene, camera);
}

