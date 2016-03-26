 var camera, scene, renderer;
    var effect, controls;
    var element, container;

    var clock = new THREE.Clock();

    init();
    animate();

    function init() {

      renderer = new THREE.WebGLRenderer();
      element = renderer.domElement;
      container = document.getElementById('example');
      container.appendChild(element);

      effect = new THREE.StereoEffect(renderer);

      scene = new THREE.Scene();

      camera = new THREE.PerspectiveCamera(90, 1, 0.001, 700);
      camera.position.set(0, 0, 0);
      scene.add(camera);

      var spacetex = THREE.ImageUtils.loadTexture('textures/patterns/space.png');
      var spacesphereGeo = new THREE.SphereGeometry(90,90,90);
      var spacesphereMat = new THREE.MeshPhongMaterial();
      spacesphereMat.map = spacetex;
      var spacesphere = new THREE.Mesh(spacesphereGeo,spacesphereMat);
  
      //spacesphere needs to be double sided as the camera is within the spacesphere
      spacesphere.material.side = THREE.DoubleSide;
      
      spacesphere.material.map.wrapS = THREE.RepeatWrapping; 
      spacesphere.material.map.wrapT = THREE.RepeatWrapping;
      spacesphere.material.map.repeat.set(1, 1);
      
      scene.add(spacesphere);

      controls = new THREE.OrbitControls(camera, element);

      initControl(controls,camera);

      function setOrientationControls(e) {
        if (!e.alpha) {
          return;
        }

        controls = new THREE.DeviceOrientationControls(camera, true);
        controls.connect();
        controls.update();

        element.addEventListener('click', fullscreen, false);
        element.addEventListener('dbclick', jump, false);

        window.removeEventListener('deviceorientation', setOrientationControls, true);
      }
      window.addEventListener('deviceorientation', setOrientationControls, true);

      var light1 = new THREE.AmbientLight(0xFFFFFF);

      scene.add(light1);

      addPage(5, 0, 0, 'textures/patterns/text.jpg', 1);

      // window.addEventListener('resize', resize, false);
      // setTimeout(resize, 1);

      // var ball = createSmallSphere(0,0,0);
      // scene.add(ball);
      // ball1 = createLargeSphere(10,10,0);
      // scene.add(ball1);
      // disappearSphere(ball1,camera);


      // var material = new THREE.LineBasicMaterial({
      //     color: 0x0000ff
      // });
      // var geometry = new THREE.Geometry();
      // geometry.vertices.push(
      //     new THREE.Vector3( 0, 0, 0 ),
      //     new THREE.Vector3( 0, 10, 0 )
      // );
      // var line = new THREE.Line( geometry, material );
      // //line.color(0xFF00FF);
      // scene.add( line );
    }

    function resize() {
      var width = container.offsetWidth;
      var height = container.offsetHeight;

      camera.aspect = width / height;
      camera.updateProjectionMatrix();

      renderer.setSize(width, height);
      effect.setSize(width, height);
    }

    function update(dt) {
      resize();

      camera.updateProjectionMatrix();

      controls.update(dt);
      updatePagePosition(camera);
    }

    function render(dt) {
      effect.render(scene, camera);
    }

    function animate(t) {
      requestAnimationFrame(animate);

      update(clock.getDelta());
      render(clock.getDelta());
    }

    function fullscreen() {
      if (container.requestFullscreen) {
        container.requestFullscreen();
      } else if (container.msRequestFullscreen) {
        container.msRequestFullscreen();
      } else if (container.mozRequestFullScreen) {
        container.mozRequestFullScreen();
      } else if (container.webkitRequestFullscreen) {
        container.webkitRequestFullscreen();
      }
    }

    function jump() {
     alert('ass');
    }