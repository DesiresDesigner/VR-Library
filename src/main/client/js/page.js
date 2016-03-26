var mesh;
var sizeOfPage;

function addPage(x, y, z, texturePath, zoom){

  sizeOfPage = x;

  var texture = THREE.ImageUtils.loadTexture(
    texturePath
  );
  texture.wrapS = THREE.RepeatWrapping;
  texture.wrapT = THREE.RepeatWrapping;
  texture.anisotropy = renderer.getMaxAnisotropy();
  var material = new THREE.MeshPhongMaterial({
    color: 0xffffff,
    specular: 0xffffff,
    shininess: 65535,
    shading: THREE.FlatShading,
    map: texture
  });

  var geometry = new THREE.PlaneGeometry(sizeOfPage + zoom, sizeOfPage + zoom);

  mesh = new THREE.Mesh(geometry, material);
  mesh.rotation.y = -Math.PI / 2;
  mesh.position.x = x;
  mesh.position.y = y;
  mesh.position.z = z;
  scene.add(mesh);

}

function updatePagePosition(camera){
   var p = camera.getWorldDirection();
  setTimeout(function(){
    mesh.position.x = p.x * sizeOfPage;
    mesh.position.y = p.y * sizeOfPage;
    mesh.position.z = p.z * sizeOfPage;
    mesh.lookAt(camera.position);

  }, 800);
}