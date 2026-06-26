<template>
  <div class="task-config-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>学习任务配置</span>
          <el-button type="primary" @click="handleAdd">新增任务</el-button>
        </div>
      </template>

      <!-- 筛选栏 -->
      <div class="filter-container">
        <el-form :inline="true" class="filter-form">
          <el-form-item label="年级">
            <el-select v-model="filterGrade" placeholder="请选择年级" clearable style="width: 120px" @change="loadData">
              <el-option label="一年级" value="一年级" />
              <el-option label="二年级" value="二年级" />
              <el-option label="三年级" value="三年级" />
              <el-option label="四年级" value="四年级" />
              <el-option label="五年级" value="五年级" />
              <el-option label="六年级" value="六年级" />
            </el-select>
          </el-form-item>
          <el-form-item label="任务类型">
            <el-select v-model="filterType" placeholder="请选择类型" clearable style="width: 120px" @change="loadData">
              <el-option label="单词学习" value="vocab" />
              <el-option label="听力训练" value="listening" />
              <el-option label="阅读理解" value="reading" />
              <el-option label="语法练习" value="grammar" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="filterEnabled" placeholder="请选择状态" clearable style="width: 100px" @change="loadData">
              <el-option label="启用" :value="true" />
              <el-option label="禁用" :value="false" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button @click="resetFilter">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 任务列表 -->
      <el-table :data="taskList" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="grade" label="年级" width="100" />
        <el-table-column prop="taskType" label="任务类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getTaskTypeTag(row.taskType)">{{ getTaskTypeLabel(row.taskType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="taskTitle" label="任务标题" />
        <el-table-column prop="taskTarget" label="目标数量" width="100" />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="enabled" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.enabled ? 'success' : 'danger'">{{ row.enabled ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadData"
        @current-change="loadData"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <!-- 编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑任务' : '新增任务'" width="600px">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="年级" prop="grade">
          <el-select v-model="formData.grade" placeholder="请选择年级" style="width: 100%">
            <el-option label="一年级" value="一年级" />
            <el-option label="二年级" value="二年级" />
            <el-option label="三年级" value="三年级" />
            <el-option label="四年级" value="四年级" />
            <el-option label="五年级" value="五年级" />
            <el-option label="六年级" value="六年级" />
          </el-select>
        </el-form-item>
        <el-form-item label="任务类型" prop="taskType">
          <el-select v-model="formData.taskType" placeholder="请选择任务类型" style="width: 100%">
            <el-option label="单词学习" value="vocab" />
            <el-option label="听力训练" value="listening" />
            <el-option label="阅读理解" value="reading" />
            <el-option label="语法练习" value="grammar" />
          </el-select>
        </el-form-item>
        <el-form-item label="任务标题" prop="taskTitle">
          <el-input v-model="formData.taskTitle" placeholder="请输入任务标题" />
        </el-form-item>
        <el-form-item label="目标数量" prop="taskTarget">
          <el-input-number v-model="formData.taskTarget" :min="1" :max="100" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="formData.sortOrder" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="状态" prop="enabled">
          <el-switch v-model="formData.enabled" active-text="启用" inactive-text="禁用" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { getTaskList, saveTask, deleteTask } from '@/api'

// 筛选条件
const filterGrade = ref('')
const filterType = ref('')
const filterEnabled = ref<boolean | null>(null)

// 分页
const pagination = reactive({
  page: 1,
  pageSize: 20,
  total: 0
})

// 任务列表
const taskList = ref<any[]>([])

// 对话框
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

// 表单数据
const formData = reactive({
  id: null as number | null,
  grade: '',
  taskType: '',
  taskTitle: '',
  taskTarget: 1,
  sortOrder: 0,
  enabled: true
})

// 表单验证规则
const formRules: FormRules = {
  grade: [{ required: true, message: '请选择年级', trigger: 'change' }],
  taskType: [{ required: true, message: '请选择任务类型', trigger: 'change' }],
  taskTitle: [{ required: true, message: '请输入任务标题', trigger: 'blur' }],
  taskTarget: [{ required: true, message: '请输入目标数量', trigger: 'blur' }]
}

// 任务类型标签
const getTaskTypeLabel = (type: string) => {
  const map: Record<string, string> = {
    vocab: '单词学习',
    listening: '听力训练',
    reading: '阅读理解',
    grammar: '语法练习'
  }
  return map[type] || type
}

const getTaskTypeTag = (type: string) => {
  const map: Record<string, string> = {
    vocab: 'primary',
    listening: 'warning',
    reading: 'success',
    grammar: 'info'
  }
  return map[type] || ''
}

// 加载数据
const loadData = async () => {
  try {
    const params: any = {
      page: pagination.page,
      pageSize: pagination.pageSize
    }
    if (filterGrade.value) params.grade = filterGrade.value
    if (filterType.value) params.taskType = filterType.value
    if (filterEnabled.value !== null) params.enabled = filterEnabled.value

    const res = await getTaskList(params)
    if (res && res.list) {
      taskList.value = res.list
      pagination.total = res.total || 0
    }
  } catch (error) {
    console.error('加载任务列表失败:', error)
    ElMessage.error('加载任务列表失败')
  }
}

// 重置筛选
const resetFilter = () => {
  filterGrade.value = ''
  filterType.value = ''
  filterEnabled.value = null
  pagination.page = 1
  loadData()
}

// 新增
const handleAdd = () => {
  isEdit.value = false
  Object.assign(formData, {
    id: null,
    grade: '',
    taskType: '',
    taskTitle: '',
    taskTarget: 1,
    sortOrder: 0,
    enabled: true
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row: any) => {
  isEdit.value = true
  Object.assign(formData, {
    id: row.id,
    grade: row.grade,
    taskType: row.taskType,
    taskTitle: row.taskTitle,
    taskTarget: row.taskTarget,
    sortOrder: row.sortOrder,
    enabled: row.enabled
  })
  dialogVisible.value = true
}

// 删除
const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定要删除这个任务吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteTask(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 提交
const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitLoading.value = true
    try {
      await saveTask(formData)
      ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
      dialogVisible.value = false
      loadData()
    } catch (error) {
      console.error('提交失败:', error)
      ElMessage.error('提交失败')
    } finally {
      submitLoading.value = false
    }
  })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.task-config-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-container {
  padding: 18px 0 0 0;
}

.filter-form {
  margin-bottom: 0;
}

.filter-form :deep(.el-form-item) {
  margin-bottom: 18px;
  margin-right: 16px;
}
</style>
