# 数学岛地图 Drawable 资源链路补充计划

> 本文档补充 `2026-03-18-island-art-tokenization-addendum.md`，聚焦地图本体的默认 drawable 资源接入链路与占位资源落地。

## 1. 目标

在不改变地图交互和业务状态的前提下：

- 为地图核心 slot 提供默认 `vector drawable` 占位
- 让 `MapArtRegistry` 优先解析 canonical drawable 资源
- 保持资源缺失时的 fallback painter 行为

## 2. 任务拆分

### Task A: Drawable 资源占位

**文件：**

- Add: `app/src/main/res/drawable/map_sea_backdrop.xml`
- Add: `app/src/main/res/drawable/route_segment_default.xml`
- Add: `app/src/main/res/drawable/route_segment_highlight.xml`
- Add: `app/src/main/res/drawable/island_locked_overlay.xml`
- Add: `app/src/main/res/drawable/island_unlocked_tint.xml`
- Add: `app/src/main/res/drawable/island_focused_ring.xml`
- Add: `app/src/main/res/drawable/island_completed_badge.xml`
- Add: `app/src/main/res/drawable/island_*_base.xml`
- Add: `app/src/main/res/drawable/island_*_icon.xml`

**结果：**

- 所有核心 slot 都有默认 drawable 占位
- 命名与 canonical slot key 一致

### Task B: Registry 资源解析

**文件：**

- Update: `app/src/main/java/com/mathisland/app/ui/components/map/MapArtRegistry.kt`
- Update: `app/src/test/java/com/mathisland/app/ui/components/map/MapArtRegistryTest.kt`

**结果：**

- registry 默认优先解析 drawable 资源
- 资源缺失时仍回退到当前 painter fallback

### Task C: 回归测试与屏幕稳定性

**文件：**

- Update: `app/src/androidTest/java/com/mathisland/app/ui/components/IslandMapCanvasTest.kt`
- Update: `app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt`
- Update: `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`

**结果：**

- 有资源和 fallback 两条路径都受测试保护
- 地图节点/航线/课程入口合同不回归

## 3. 推荐顺序

1. Drawable 资源占位
2. Registry 资源解析
3. 回归测试

## 4. 验收命令

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat connectedDebugAndroidTest`
- `./gradlew.bat assembleDebug`
