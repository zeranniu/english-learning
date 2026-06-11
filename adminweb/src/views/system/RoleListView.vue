<template>
  <div class="page-container">
    <el-card shadow="hover">
      <template #header><div style="display:flex;justify-content:space-between;align-items:center"><span>角色管理</span><el-button type="primary" size="small" @click="showDialog(null)">新增角色</el-button></div></template>
      <el-table :data="roles">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="roleName" label="角色名称" />
        <el-table-column prop="roleCode" label="角色编码" />
        <el-table-column prop="description" label="描述" />
        <el-table-column label="状态" width="80"><template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="showMenuDialog(row)">分配菜单</el-button>
            <el-button link type="primary" size="small" @click="showDialog(row)">编辑</el-button>
            <el-popconfirm title="确定删除？" @confirm="handleDelete(row.id)"><template #reference><el-button link type="danger" size="small">删除</el-button></template></el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="dialogVisible" :title="editRole ? '编辑角色' : '新增角色'" width="400px">
      <el-form :model="roleForm" label-width="80px">
        <el-form-item label="角色名称"><el-input v-model="roleForm.roleName" /></el-form-item>
        <el-form-item label="角色编码"><el-input v-model="roleForm.roleCode" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="roleForm.description" type="textarea" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSave">保存</el-button></template>
    </el-dialog>
    <el-dialog v-model="menuDialogVisible" title="分配菜单权限" width="400px">
      <el-tree :data="menuTree" show-checkbox node-key="id" ref="menuTreeRef" :props="{ label: 'name', children: 'children' }" />
      <template #footer><el-button @click="menuDialogVisible = false">取消</el-button><el-button type="primary" @click="handleAssignMenus">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getRoleList, saveRole, deleteRole, getMenuTree, getRoleMenus, assignMenus } from '@/api'
import { ElMessage } from 'element-plus'

const roles = ref<any[]>([])
const menuTree = ref<any[]>([])
const dialogVisible = ref(false)
const menuDialogVisible = ref(false)
const editRole = ref<any>(null)
const roleForm = ref<any>({})
const menuTreeRef = ref()
const currentRoleId = ref(0)

async function loadRoles() { try { roles.value = await getRoleList() } catch {} }
async function loadMenus() { try { menuTree.value = await getMenuTree() } catch {} }

function showDialog(role: any) {
  editRole.value = role
  roleForm.value = role ? { ...role } : { roleName: '', roleCode: '', description: '', status: 1 }
  dialogVisible.value = true
}

async function handleSave() {
  await saveRole(roleForm.value)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadRoles()
}

async function handleDelete(id: number) { await deleteRole(id); ElMessage.success('删除成功'); loadRoles() }

async function showMenuDialog(role: any) {
  currentRoleId.value = role.id
  await loadMenus()
  menuDialogVisible.value = true
  setTimeout(async () => {
    const ids: number[] = await getRoleMenus(role.id)
    menuTreeRef.value?.setCheckedKeys(ids)
  }, 100)
}

async function handleAssignMenus() {
  const ids = menuTreeRef.value?.getCheckedKeys() || []
  await assignMenus(currentRoleId.value, ids)
  ElMessage.success('分配成功')
  menuDialogVisible.value = false
}

onMounted(() => { loadRoles(); loadMenus() })
</script>
