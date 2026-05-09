<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { categoryApi, type Category } from '../../api/category'
import AdminSidebar from '../../components/layout/AdminSidebar.vue'

const categories = ref<Category[]>([])
const newName = ref('')
const newDesc = ref('')
const editing = ref<number | null>(null)
const editName = ref('')
const editDesc = ref('')

async function load() { const r = await categoryApi.list(); categories.value = r.data.data }
async function create() { if (newName.value.trim()) { await categoryApi.create({ name: newName.value, description: newDesc.value }); newName.value = ''; newDesc.value = ''; load() } }
async function update(id: number) { if (editName.value.trim()) { await categoryApi.update(id, { name: editName.value, description: editDesc.value }); editing.value = null; load() } }
async function remove(id: number) { if (confirm('确定删除？')) { await categoryApi.delete(id); load() } }
function startEdit(c: Category) { editing.value = c.id; editName.value = c.name; editDesc.value = c.description || '' }

onMounted(load)
</script>

<template>
  <div class="max-w-6xl mx-auto px-4 py-8">
    <div class="flex gap-8">
      <AdminSidebar />
      <div class="flex-1 min-w-0">
        <h1 class="text-2xl font-black mb-6" style="font-family: var(--font-display)">分类管理</h1>
        <!-- Add -->
        <div class="flex gap-2 mb-4">
          <input v-model="newName" @keyup.enter="create" placeholder="新分类名称"
                 class="px-3 py-2 rounded-lg text-sm border outline-none flex-1"
                 style="border-color: var(--border-color); background-color: var(--bg-card); color: var(--text-primary)" />
          <input v-model="newDesc" @keyup.enter="create" placeholder="描述（选填）"
                 class="px-3 py-2 rounded-lg text-sm border outline-none flex-1 hidden sm:block"
                 style="border-color: var(--border-color); background-color: var(--bg-card); color: var(--text-primary)" />
          <button @click="create" class="px-4 py-2 rounded-lg text-sm font-bold text-white transition-all hover:scale-105"
                  style="background: var(--accent-pink)">添加</button>
        </div>
        <!-- List -->
        <div class="space-y-2">
          <div v-for="cat in categories" :key="cat.id" class="flex items-center gap-2 p-3 rounded-lg"
               style="background-color: var(--bg-card); border: 1px solid var(--border-color)">
            <template v-if="editing === cat.id">
              <input v-model="editName" class="px-2 py-1 rounded text-sm border outline-none flex-1"
                     style="border-color: var(--border-color); background-color: var(--bg-secondary); color: var(--text-primary)" />
              <input v-model="editDesc" placeholder="描述" class="px-2 py-1 rounded text-sm border outline-none flex-1"
                     style="border-color: var(--border-color); background-color: var(--bg-secondary); color: var(--text-primary)" />
              <button @click="update(cat.id)" class="text-xs px-2 py-1 rounded" style="color: green">保存</button>
              <button @click="editing = null" class="text-xs px-2 py-1 rounded" style="color: var(--text-secondary)">取消</button>
            </template>
            <template v-else>
              <span class="flex-1 font-medium text-sm" style="color: var(--text-primary)">{{ cat.name }}</span>
              <span class="text-xs hidden sm:block" style="color: var(--text-secondary)">{{ cat.slug }}</span>
              <button @click="startEdit(cat)" class="text-xs px-2 py-1 rounded"
                      style="color: var(--accent-pink); background-color: color-mix(in srgb, var(--accent-pink) 10%, transparent)">编辑</button>
              <button @click="remove(cat.id)" class="text-xs px-2 py-1 rounded"
                      style="color: var(--accent-red); background-color: color-mix(in srgb, var(--accent-red) 10%, transparent)">删除</button>
            </template>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
