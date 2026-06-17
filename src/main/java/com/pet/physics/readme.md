# physics Package

Handles all movement and physical behavior of the desktop pet, including position updates, forces, and collision logic.

## Files

| File | Purpose |
|------|---------|
| `PhysicsEngine.java` | Core physics controller. Updates position, velocity, and physics state. |
| `Gravity.java` | Applies gravity force to the pet. |
| `CollisionDetector.java` | Detects and handles collisions with screen boundaries or other objects. |

## Responsibilities

- Manage position and velocity
- Apply forces (gravity, impulses)
- Detect and resolve collisions
- Enable or disable physics simulation
- Provide movement data to the pet window and animations

## Overview

The `physics` package is responsible for simulating realistic movement and interactions for the desktop pet. It maintains the pet's physical state, applies forces such as gravity, handles collisions with screen boundaries or other objects, and supplies movement information to the rendering and animation systems.