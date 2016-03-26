function createSphere(radius, segments) {
	return new THREE.Mesh(
		new THREE.SphereGeometry(radius, segments, segments),
		new THREE.MeshPhongMaterial({
				/*map:         THREE.ImageUtils.loadTexture('images/2_no_clouds_4k.jpg'),*/
				color: new THREE.Color('white'),
				wireframe: true
				/*bumpMap:     THREE.ImageUtils.loadTexture('images/elev_bump_4k.jpg'),
				bumpScale:   0.005,
				specularMap: THREE.ImageUtils.loadTexture('images/water_4k.png'),
				specular:    new THREE.Color('grey')*/
		})
	);
}

function createSmallSphere(x,y,z){
	var sphere = createSphere(1,10);
	sphere.position.x = x;
	sphere.position.y = y;
	sphere.position.z = z;
	return sphere;
}

function createLargeSphere(x,y,z){
	var sphere = createSphere(3,20);
	sphere.position.x = x;
	sphere.position.y = y;
	sphere.position.z = z;
	return sphere;
}

function disappearSphere(sphere, camera){
	console.log(sphere.position.x,sphere.position.y,sphere.position.z);
	console.log(camera.position.x,camera.position.y,camera.position.z);
	var distance = Math.sqrt(sphere.position.x*camera.position.x + sphere.position.y*camera.position.y + sphere.position.z*camera.position.z);
	if(distance < 1){
		sphere.opacity = distance;
	}
}