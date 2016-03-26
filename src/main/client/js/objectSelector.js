
function ObjectSelector() {
	this.__raycaster = new THREE.Raycaster();
	this.__screenCenter = { x: 0, y: 0};
	this.__lastIntersected = null;

	this.__createFocusAcquiredEvent = function(obj) {
		return { 
			type: 'focusacquired',
			focusTarget: obj
		};
	}

	this.__createFocusLostEvent = function(obj) {
		return { 
			type: 'focuslost',
			focusTarget: obj
		};
	}

	this.process = function(camera, scene) {
		this.__raycaster.setFromCamera(this.__screenCenter, camera);

		var intersects = this.__raycaster.intersectObjects(scene.children);

		if (intersects.length) {
			if (this.__lastIntersected != intersects[0].object) {
				scene.dispatchEvent(this.__createFocusLostEvent(this.__lastIntersected));
				this.__lastIntersected = null;
				
				this.__lastIntersected = intersects[0].object;
				scene.dispatchEvent(this.__createFocusAcquiredEvent());
			}
		} else {
			scene.dispatchEvent(this.__createFocusLostEvent(this.__lastIntersected));
			this.__lastIntersected = null;
		}
	}
}
