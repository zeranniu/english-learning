<template>
  <el-container style="height: 100vh">
    <el-aside :width="isCollapse ? '64px' : '220px'" style="transition: width 0.3s; background: #304156; overflow: hidden">
      <div style="height: 60px; display: flex; align-items: center; justify-content: center; color: #fff; font-size: 18px; font-weight: bold; border-bottom: 1px solid rgba(255,255,255,0.1)">
        <span v-if="!isCollapse">英语学习管理端</span>
        <span v-else>EL</span>
      </div>
      <el-menu
        :default-active="route.path"
        :collapse="isCollapse"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
        router
        style="border-right: none"
        class="admin-sidebar-menu"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataBoard /></el-icon>
          <template #title>数据看板</template>
        </el-menu-item>
        <el-sub-menu index="user">
          <template #title><el-icon><User /></el-icon><span>用户管理</span></template>
          <el-menu-item index="/user/list">学生列表</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="system">
          <template #title><el-icon><Setting /></el-icon><span>系统管理</span></template>
          <el-menu-item index="/system/role">角色管理</el-menu-item>
          <el-menu-item index="/system/menu">菜单管理</el-menu-item>
          <el-menu-item index="/system/log">操作日志</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="content">
          <template #title><el-icon><Collection /></el-icon><span>题库管理</span></template>
          <el-menu-item index="/content/vocab">单词库</el-menu-item>
          <el-menu-item index="/content/listening">听力题库</el-menu-item>
          <el-menu-item index="/content/reading">阅读题库</el-menu-item>
          <el-menu-item index="/content/grammar">语法题库</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="stats">
          <template #title><el-icon><TrendCharts /></el-icon><span>数据统计</span></template>
          <el-menu-item index="/stats/mistake">错题统计</el-menu-item>
          <el-menu-item index="/stats/data">学习统计</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="display: flex; align-items: center; justify-content: space-between; border-bottom: 1px solid #eee; background: #fff">
        <el-icon style="cursor: pointer; font-size: 20px" @click="isCollapse = !isCollapse">
          <Fold v-if="!isCollapse" />
          <Expand v-else />
        </el-icon>
        <div style="display: flex; align-items: center; gap: 12px">
          <span>{{ authStore.adminInfo?.nickname || '管理员' }}</span>
          <el-dropdown @command="handleCommand">
            <el-icon style="cursor: pointer"><ArrowDown /></el-icon>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main style="background: #f0f2f5; overflow-y: auto">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const isCollapse = ref(false)

function handleCommand(cmd: string) {
  if (cmd === 'logout') {
    authStore.logout()
    router.push('/login')
  }
}
</script>

<style>
.admin-sidebar-menu {
  --admin-menu-base-padding: 20px;
  --admin-menu-icon-width: 28px;
  --admin-menu-level-indent: 16px;
}

/* 一级菜单与一级子菜单标题保持一致的容器缩进 */
.admin-sidebar-menu > .el-menu-item,
.admin-sidebar-menu > .el-sub-menu > .el-sub-menu__title {
  padding-left: var(--admin-menu-base-padding) !important;
}

/*
  一级菜单带图标，文字起点 = 容器缩进 + 图标宽度；
  二级菜单不带图标，需要先补齐图标宽度，再叠加层级缩进，避免层级视觉反转。
*/
.admin-sidebar-menu > .el-sub-menu .el-menu--inline > .el-menu-item {
  padding-left: calc(var(--admin-menu-base-padding) + var(--admin-menu-icon-width) + var(--admin-menu-level-indent)) !important;
  padding-right: 20px !important;
}
</style>
