# Math Island 发布候选说明（2026-03-31）

## 候选基线

- 版本：`0.1.0`
- 候选提交：`a6a695a`
- 目标设备：Android 平板横屏，推荐 `1280x800`，API 33+

## 本轮完成内容

- 主路径页面收口完成：`Home / Map / Level / Reward / Chest / Parent`
- 地图回流链路统一：
  - 奖励页 next-step
  - 地图顶部回流摘要
  - 左侧岛屿列表 handoff
  - 右侧岛屿面板 handoff
- lesson 交互反馈统一：
  - `ready / retry / confirmed / timeout`
  - 左侧 support rail 与右侧答题舞台语言、强调色、节奏一致
- 外围页面设备侧布局收口：
  - Home section anchor
  - Chest 可滚动与贴纸网格
  - Parent Gate 自动换行与 Parent Summary 内部滚动

## 自动化验收结果

- `testDebugUnitTest`：通过
- `assembleDebug`：通过
- `connectedDebugAndroidTest`：通过，`58 tests`

## 当前稳定合同

- 地图进入课程：`panel-start-<lessonId>`
- 首页入口：
  - `home-continue-adventure`
  - `home-open-map`
  - `home-open-chest`
  - `home-open-parent`
- 家长验证答案：`parent-answer-<value>`
- 宝箱返回地图：`chest-open-map`

## 已知非阻塞项

- 更重的地图动画和正式美术资源仍属于增强项，不阻塞当前候选版
- lesson 题目区仍可继续做更强动画，但当前交互与状态提示已完整可用
- focused emulator regression 脚本保留为 UTP 波动时的稳定 fallback

## 建议发布前动作

1. 按 [manual-testing-checklist.md](/D:/Practice/codex/math-island/docs/manual-testing-checklist.md) 走一遍人工验收。
2. 保留这次 `connectedDebugAndroidTest` 全绿结果作为候选版自动化验收基线。
3. 若无 blocker，再决定是否进入下一轮内容增强或正式发版打包。
