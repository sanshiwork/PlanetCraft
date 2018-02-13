// 调试开关
var debug = false;
var url = "ws://10.12.16.178/planet/craft/socket";
// var url = "ws://txpromoter.changyou.com/planet/craft/socket";
// 游戏场景
var camera, scene, renderer, planet;
var rayCaster = new THREE.Raycaster();
var mouse = new THREE.Vector2();
const clock = new THREE.Clock();
var heartbeat = 0;
// 帧率监控
var stats;
// UI
var gui;

// 加载进度
var spinner = (function () {
    var spinner = new Spinner().spin();
    document.getElementById('container').appendChild(spinner.el);
    return spinner;
})();

// websocket
var ws = (function () {
    // 连接服务器
    var ws = new WebSocket(url);

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
        } else if (packet.id === -1) {
            // Heartbeat Packet
            console.log("heartbeat ack.")
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

// GUI
var controls = new function () {
    this.color = 0x2534a4;
    this.pencil = '0';
    this.distance = 6;
    this.rotateX = 0;
    this.rotateY = 0;
    this.rotateZ = 0;
    this.resetCamera = function () {
        this.distance = 6;
        camera.position.set(0, controls.distance, 0);
        console.log(gui);
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
    position.add(controls, 'distance', 0.5, 6).step(0.01).onChange(function () {
        camera.position.y = controls.distance;
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

// 初始化场景
function init(data) {
    // 相机
    camera = new THREE.PerspectiveCamera(90, window.innerWidth / window.innerHeight, 0.1, 1000);
    camera.position.set(0, controls.distance, 0);
    camera.lookAt(new THREE.Vector3(0, 0, 0));

    // 场景
    scene = new THREE.Scene();

    // 辅助线
    if (debug) {
        scene.add(new THREE.AxesHelper(20));
    }


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
        vertexColors: THREE.FaceColors
    });
    planet = new THREE.Mesh(geometry, material);
    scene.add(planet);

    renderer = new THREE.WebGLRenderer();
    renderer.setClearColor(0x000020);
    renderer.setPixelRatio(window.devicePixelRatio);
    renderer.setSize(window.innerWidth, window.innerHeight);

    var container = document.getElementById('container');
    container.appendChild(renderer.domElement);

    if (debug) {
        stats = new Stats();
        container.appendChild(stats.dom);
    }

    // 屏幕大小变化
    window.addEventListener('resize', function () {
        camera.aspect = window.innerWidth / window.innerHeight;
        camera.updateProjectionMatrix();

        renderer.setSize(window.innerWidth, window.innerHeight);
    }, false);

    // 加载完成
    spinner.spin();
}

// 逐帧渲染
function animate() {
    requestAnimationFrame(animate);
    if (debug) {
        stats.update();
    }
    render();

}

// 渲染
function render() {
    renderer.render(scene, camera);
    heartbeat += clock.getDelta();
    // 发送心跳，防止断连（NGINX），间隔20秒
    if (heartbeat >= 20) {
        console.log("heartbeat.");
        ws.send(JSON.stringify({
            id: -1,
            data: {}
        }));
        heartbeat = 0;
    }
}

// 鼠标/触摸操作适配
var mc;
(function (element) {
    mc = new Hammer(element);
    mc.add(new Hammer.Pan({direction: Hammer.DIRECTION_ALL, threshold: 0}));
    mc.get('pinch').set({enable: true});
    // 拖动
    mc.on("pan", function (ev) {
        var speed = camera.position.y * 0.1;
        camera.position.x -= ev.velocityX * speed;
        camera.position.z -= ev.velocityY * speed;
        if (camera.position.x < -10) {
            camera.position.x = -10;
        }
        if (camera.position.x > 10) {
            camera.position.x = 10;
        }
        if (camera.position.z < -6) {
            camera.position.z = -6;
        }
        if (camera.position.z > 6) {
            camera.position.z = 6;
        }
    });
    // 缩放
    mc.on("pinch", function (ev) {
        var speed = camera.position.y * 0.005;
        if (ev.scale > 1) {
            camera.position.y -= ev.distance * speed;
        } else if (ev.scale < 1) {
            camera.position.y += ev.distance * speed;
        }
        if (camera.position.y < 0.5) {
            camera.position.y = 0.5;
        }
        if (camera.position.y > 6) {
            camera.position.y = 6;
        }
    });
    // 触摸/点击
    mc.on("tap", function (ev) {
        var event = ev.srcEvent;
        var x = event.clientX ? event.clientX : event.layerX;
        var y = event.clientY ? event.clientY : event.layerY;
        // 兼容触摸事件和点击事件
        mouse.x = ( x / renderer.domElement.clientWidth ) * 2 - 1;
        mouse.y = -( y / renderer.domElement.clientHeight ) * 2 + 1;
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
    });


})(document.getElementById('container'));

