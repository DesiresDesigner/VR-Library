/**
 * Created by vesel on 2016-03-26.
 */
'use strict';

const standardLength = 1;
const minimalStep = 0.05;
const initialStep = 1;

function preallocateNodes(graph) {
    for (let node of graph.nodes) {
        node['position'] = new Point(Math.random() * 5, Math.random() * 5, Math.random() * 5);
    }
}
/**
 *
 * @param nodes
 * @returns {Map}
 */
function makeNodeMap(nodes) {
    const map = new Map();
    for (let node of nodes) {
        map.set(node.id, node);
    }
    return map;
}

/**
 * @param graph
 * @param nodeMap {Map}
 * @returns {number}
 */
function calculateCost(graph, nodeMap) {
    let loss = 0;
    for (let edge of graph.edges) {
        const p1 = nodeMap.get(edge.source).position;
        const p2 = nodeMap.get(edge.target).position;
        const squareLength = p1.distanceTo(p2);
        loss += Math.abs(standardLength * standardLength - squareLength);
    }
    // const gNodes = graph.nodes;
    // for (let i = 0, len = gNodes.length, len1 = len - 1; i < len1; ++i) {
    //     for (let j = i + 1; j < len; ++j) {
    //         const length = gNodes[i].position.distanceTo(gNodes[j].position);
    //
    //     }
    // }

    return loss;
}

/**
 * Callback for adding two numbers.
 *
 * @callback OnInterationCallback
 * @param graph - graph.
 */

/**
 *
 * @param graphJSON {string}
 * @param onIteration {OnInterationCallback}
 */
function embedGraph(graphJSON, onIteration) {
    const graph = JSON.parse(graphJSON);
    preallocateNodes(graph);
    const nodeMap = makeNodeMap(graph.nodes);
    let fitness = calculateCost(graph, nodeMap);
    for (let step = initialStep; step > minimalStep; step *= 0.61803398875) {
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
                        const total = calculateCost(graph, nodeMap);
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
}
//
// embedGraph("dffgd{}{}}", graph => {
//     const { nodes, edges } = graph;
//     for (let node of nodes) {
//
//     }
// });