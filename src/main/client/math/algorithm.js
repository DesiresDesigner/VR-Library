/**
 * Created by vesel on 2016-03-26.
 */

// basic edge length = 1

const standardLength = 1;

function preallocateNodes(graph) {
    for (let node of graph.nodes) {
        node['position'] = {
            x: Math.random() * 5,
            y: Math.random() * 5,
            z: Math.random() * 5
        }
    }
}

function makeNodeMap(nodes) {
    const map = new Map();
    for (let node of nodes) {
        map.set(node.id, node);
    }
    return map;
}

/**
 *
 * @param nodeMap {Map<string, Point>}
 * @param edges
 */
function calculateCost(nodeMap, edges) {
    const nodes = new Map();
    for (let edge of graph.edges) {
        const source = nodeMap.get(edge.source);
        const target = nodeMap.get(edge.target);
    }

}

/**
 *
 * @param graphJSON {string}
 * @param onIteration OnInterationCallback
 *  * This callback is displayed as a global member.
 * *  @callback OnInterationCallback
 */
function embedGraph(graphJSON, onIteration) {
    const graph = JSON.parse(graphJSON);
    preallocateNodes(graph);
    const nodeMap = makeNodeMap(graph.nodes);
    let fitness = calculateCost(nodeMap, graph.edges);
    for (let step = 10; step > minStep; step *= 0.61803398875) {
        for (let node of graph.nodes) {
            const initialPosition = {
                x: node.position.x,
                y: node.position.y,
                z: node.position.z
            };
            let hasChanged = false;
            for (let max = step, x = -max; x <= max; x += step) {
                node.position.x = initialPosition.x + x;
                for (let y = -max; y <= max; y += step) {
                    node.position.y = initialPosition.y + y;
                    for (let z = -max; z <= max; z += step) {
                        node.position.z = initialPosition.z + z;
                        const total = calculateCost(nodeMap, graph.edges);
                        if (total < fitness) {
                            hasChanged = true;
                            fitness = total;
                        }
                    }
                }
            }
            if (!hasChanged) {
                node.position = initialPosition;
            }
            onIteration(graph);
        }

    }
    return calculateCost(graph);
}
/**

 */