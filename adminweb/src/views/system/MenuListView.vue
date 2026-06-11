<template>
  <div class="page-container">
    <el-card shadow="hover">
      <template #header><div style="display:flex;justify-content:space-between;align-items:center"><span>菜单管理</span><el-button type="primary" size="small" @click="showDialog(null)">新增菜单</el-button></div></template>
      <el-table :data="menus" row-key="id" border default-expand-all>
        <el-table-column prop="name" label="菜单名称" />
        <el-table-column prop="path" label="路由路径" />
        <el-table-column prop="component" label="组件路径" />
        <el-table-column prop="icon" label="图标" width="80" />
        <el-table-column prop="sortOrder" label="排序" width="60" />
        <el-table-column label="类型" width="80"><template #default="{ row }"><el-tag size="small">{{ row.type === 1 ? '菜单' : row.type === 2 ? '按钮' : '外链' }}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="showDialog(row)">编辑</el-button>
            <el-popconfirm title="确定删除？" @confirm="handleDelete(row.id)"><template #reference><el-button link type="danger" size="small">删除</el-button></template></el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="dialogVisible" :title="editMenu ? '编辑菜单' : '新增菜单'" width="500px">
      <el-form :model="menuForm" label-width="80px">
        <el-form-item label="菜单名称"><el-input v-model="menuForm.name" /></el-form-item>
        <el-form-item label="父级ID"><el-input-number v-model="menuForm.parentId" :min="0" /></el-form-item>
        <el-form-item label="路由路径"><el-input v-model="menuForm.path" /></el-form-item>
        <el-form-item label="组件路径"><el-input v-model="menuForm.component" /></el-form-item>
        <el-form-item label="图标"><el-input v-model="menuForm.icon" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="menuForm.sortOrder" :min="0" /></el-form-item>
        <el-form-item label="权限标识"><el-input v-model="menuForm.permission" /></el-form-item>
        <el-form-item label="类型"><el-radio-group v-model="menuForm.type"><el-radio :value="1">菜单</el-radio><el-radio :value="2">按钮</el-radio><el-radio :value="3">外链</el-radio></el-radio-group></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSave">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getMenuTree, saveMenu, deleteMenu } from '@/api'
import { ElMessage } from 'element-plus'

const menus = ref<any[]>([])
const dialogVisible = ref(false)
const editMenu = ref<any>(null)
const menuForm = ref<any>({})

async function loadMenus() { try { menus.value = await getMenuTree() } catch {} }
function showDialog(menu: any) { editMenu.value = menu; menuForm.value = menu ? { ...menu } : { parentId: 0, name: '', path: '', component: '', icon: '', sortOrder: 0, permission: '', type: 1, visible: true }; dialogVisible.value = true }
async function handleSave() { await saveMenu(menuForm.value); ElMessage.success('保存成功'); dialogVisible.value = false; loadMenus() }
async function handleDelete(id: number) { await deleteMenu(id); ElMessage.success('删除成功'); loadMenus() }

onMounted(loadMenus)
</script>
