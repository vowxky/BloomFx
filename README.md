# BloomFX

A lightweight bloom post-processing system for Minecraft using Lodestone.

> ⚠️ This is a **backport** of the bloom post-processing effect introduced in Lodestone 1.21+, adapted for use in **1.20.1** fabric.

## Requirements

- **Minecraft** 1.20.1  
- **[Lodestone Mod](https://www.curseforge.com/minecraft/mc-mods/lodestone)** (required)

## Usage

This mod provides a centralized static class called `BloomFxAPI` to access and control the bloom effect.

### Control

To enable the bloom effect:

```java
BloomFxAPI.getBloom().enable();
```

To permanently disable the effect (and prevent it from being enabled again):

```java
BloomFxAPI.getBloom().setForceDisabled();
```

Calling `setForceDisabled()` will automatically deactivate the effect if it's active, and any future calls to `enable()` will be ignored.

---

### 🔧 RenderLayer Output Requirement

In order for the bloom effect to work correctly, **your custom `RenderLayer` must use the output target from the bloom system**.

You can use **either** of the following as your output:

```java
ShaderRegistryBloom.getBloom().getBloomOutput()
// or
ShaderRegistryBloom.getBloom().getPhysicalBloomOutput()
```

This ensures your rendered content is captured by the bloom pipeline.
