# ForceWechatDarkMode 开启微信深色主题/暗色主题/深色模式/暗色模式

微信曾短暂的推出过深色主题（大约在 7.0.10 版），很快又取消了，但其实后续的版本已内置了一套深色主题，只是没有提供开关。启用该模块后即可开启微信内置的深色主题。

需要系统支持深色主题。Android 10 全面支持，Android 9 和 8 部分系统支持。

## Bug 收集

各位用户烦请关注 Issue 列表，如发现相同的 Bug 请跟帖，发现新 Bug 请提交新 Issue。

提交 Issue 前请确保已经过测试：除待测试的模块外，关闭其余所有模块，确认问题仍存在。

提交 Issue 时请注明以下信息
+ 机型
+ 系统及 Android 版本（例如 H2OS Android 10）
+ 框架类型（EdXposed / 太极阴 / 太极阳 / rovo89 原版 Xposed 等）及版本
+ 微信详细版本（我-设置-关于微信-连续点击微信图标即可看到）以及是 Play 还是国内版
+ 视情况可能需要提供日志和提取的微信安装包


## 更新日志

#### 2020.03.29
+ 代码重构，但核心代码不变
+ 改用 [RemotePreferences](https://github.com/apsun/RemotePreferences) 处理配置，可能解决了存储重定向带来的潜在问题
+ 感谢 [Palatis](https://github.com/Palatis) 的贡献（pull [#8](https://github.com/chouqibao/ForceWechatDarkMode/pull/8)）

#### 2020.03.05
+ 增加用户界面
+ 增加检测系统深色主题状态
+ 增加保持微信深色主题开关
+ 增加保存日志至文件选项（保存在 `/sdcard/Android/data/com.xiangteng.xposed.forcewechatdarkmode/log/`）
+ 删除针对特定微信版本的 hook（可能导致在相关版本上失效，如果有的话请报告）

#### 2020.03.03 再次更改 hook 点
在微信读取配置（MMKV）时进行 hook，将深色主题打开。

#### 2020.03.03 更改 hook 点
更改 hook 点后理论上能适配更多版本和机型，但仍不稳定。

#### 2020.03.02 第一版