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
        <template v-for="menu in menus" :key="menu.id">
          <el-menu-item v-if="!menu.children" :index="menu.path">
            <el-icon><component :is="getIcon(menu.icon)" /></el-icon>
            <template #title>{{ menu.name }}</template>
          </el-menu-item>
          <el-sub-menu v-else :index="menu.path">
            <template #title>
              <el-icon><component :is="getIcon(menu.icon)" /></el-icon>
              <span>{{ menu.name }}</span>
            </template>
            <el-menu-item v-for="child in menu.children" :key="child.id" :index="child.path">
              {{ child.name }}
            </el-menu-item>
          </el-sub-menu>
        </template>
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
            <el-icon style="cursor: pointer; font-size: 16px"><ArrowDown /></el-icon>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon> 个人中心
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon> 退出登录
                </el-dropdown-item>
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
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { getUserMenus } from '@/api'
import {
  ArrowDown, User, SwitchButton, Fold, Expand,
  DataBoard, Setting, Collection, TrendCharts, Upload,
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const isCollapse = ref(false)
const menus = ref<any[]>([])

const iconMap: Record<string, any> = {
  DataBoard, User, Setting, Collection, TrendCharts,
}

function getIcon(iconName: string) {
  return iconMap[iconName] || DataBoard
}

onMounted(async () => {
  try {
    menus.value = await getUserMenus()
  } catch {
    console.error('加载菜单失败')
  }
})

function handleCommand(cmd: string) {
  if (cmd === 'profile') {
    router.push('/system/profile')
  } else if (cmd === 'logout') {
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

.admin-sidebar-menu > .el-menu-item,
.admin-sidebar-menu > .el-sub-menu > .el-sub-menu__title {
  padding-left: var(--admin-menu-base-padding) !important;
}

.admin-sidebar-menu > .el-sub-menu .el-menu--inline > .el-menu-item {
  padding-left: calc(var(--admin-menu-base-padding) + var(--admin-menu-icon-width) + var(--admin-menu-level-indent)) !important;
  padding-right: 20px !important;
}
</style>
