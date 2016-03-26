function initControl(controls,camera){
    controls.rotateUp(Math.PI / 4);
    controls.target.set(
        camera.position.x + 0.1,
        camera.position.y,
        camera.position.z
      );
    controls.noZoom = false;
    controls.zoomSpeed = 5.0;
    controls.noPan = false;
	controls.keyPanSpeed = 100.0;
}