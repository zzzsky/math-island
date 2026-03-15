# 数学岛地图与岛屿交互体验实施补充计划

> 本文档是对 `2026-03-13-math-island-android-tablet-implementation.md` 的增量补充，不替代原计划。

## 1. 目标

在现有 Android tablet MVP 上补齐地图/岛屿交互体验，使地图从静态课程列表升级为带焦点切换、岛屿详情面板和通关回流反馈的主世界入口。

## 2. 对应原计划映射

本补充计划主要细化和延展以下原计划内容：

- `TabletNavGraph / TabletScaffold / WindowProfile`
- `MapViewModel / MapTabletScreen`
- `IslandOverlaySheet / IslandViewModel`
- `RewardOverlay`
- `Home -> Map -> Level -> Reward -> Map` 主循环验收

现有代码中未完全落到原命名的部分，可以沿当前实际结构实现，但最终职责应与原计划对齐。

## 3. 新增任务拆分

### Task A: 地图节点交互

**目标：**

- 引入地图场景组件，展示岛屿节点和航线
- 支持 `locked / unlocked / focused / completed` 四态
- 支持点击节点切换当前焦点岛

**建议文件：**

- Create: `app/src/main/java/com/mathisland/app/feature/map/MapSceneCanvas.kt`
- Update: `app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt`
- Update: `app/src/main/java/com/mathisland/app/feature/map/MapViewModel.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt`

**完成标准：**

- 地图节点可点击
- 焦点岛切换后右侧内容同步
- 未解锁岛和已完成岛有清晰视觉差异

### Task B: 岛屿详情面板

**目标：**

- 为焦点岛提供固定详情面板
- 展示岛屿描述、进度、课程列表和主 CTA
- 区分课程状态：`未开始 / 可开始 / 已完成 / 新解锁`

**建议文件：**

- Create: `app/src/main/java/com/mathisland/app/feature/map/IslandDetailPanel.kt`
- Update: `app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt`
- Update: `app/src/main/java/com/mathisland/app/feature/map/MapViewModel.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/map/IslandDetailPanelTest.kt`

**完成标准：**

- 焦点岛信息完整显示
- 主 CTA 指向推荐课程
- 点击课程卡进入指定 lesson

### Task C: 地图推进反馈

**目标：**

- 课程结算回地图后显示一次性反馈
- 支持 `新岛解锁 / 新课解锁 / 星星增加 / 宝箱提示`
- 反馈在 UI 消费后清空，不重复播放

**建议文件：**

- Create: `app/src/main/java/com/mathisland/app/feature/map/MapProgressFeedback.kt`
- Update: `app/src/main/java/com/mathisland/app/feature/map/MapViewModel.kt`
- Update: `app/src/main/java/com/mathisland/app/MathIslandApp.kt`
- Update: `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/map/MapProgressFeedbackTest.kt`

**完成标准：**

- 通关返回地图后出现至少一类路线或节点解锁反馈
- 星星或宝箱奖励有明确提示
- 反馈只出现一次

## 4. 并行执行策略

这三条任务可以并发推进，但应遵守以下边界：

- `Task A` 负责地图画布与岛屿节点态
- `Task B` 负责详情面板与课程入口
- `Task C` 负责回流反馈与一次性事件消费

共享的数据汇合层建议集中在 `MapViewModel` 或新增 `MapInteractionViewModel`，避免三条线直接改动课程 controller。

## 5. 推荐实现顺序

如果资源允许并行：

1. A / B / C 同时开始
2. 先落数据模型和 UI state 契约
3. 再分别实现 UI 和测试
4. 最后合流跑整套 `connectedDebugAndroidTest`

如果必须串行收口：

1. `Task A`
2. `Task B`
3. `Task C`

## 6. 验收命令

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat connectedDebugAndroidTest`
- 可选聚焦：
  - `./gradlew.bat connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.map.MapTabletScreenTest"`
  - `./gradlew.bat connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.MathIslandTabletFlowTest"`

## 7. 进度记录方式

建议后续开发按以下节奏记录：

- 完成 Task A 后，更新本文件状态并记录测试结果
- 完成 Task B 后，更新本文件状态并记录测试结果
- 完成 Task C 后，更新本文件状态并记录测试结果

如果需要进一步拆分为多个 worktree，可直接按 Task A / B / C 建立分支，减少冲突。
