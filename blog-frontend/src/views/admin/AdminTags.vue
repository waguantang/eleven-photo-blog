<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { tagApi, type Tag } from '../../api/category'
import AdminSidebar from '../../components/layout/AdminSidebar.vue'

const tags = ref<Tag[]>([])
const newName = ref('')
const editing = ref<number | null>(null)
const editName = ref('')

async function load() { const r = await tagApi.list(); tags.value = r.data.data }
async function create() { if (newName.value.trim()) { await tagApi.create({ name: newName.value }); newName.value = ''; load() } }
async function update(id: number) { if (editName.value.trim()) { await tagApi.update(id, { name: editName.value }); editing.value = null; load() } }
async function remove(id: number) { if (confirm('确定删除？')) { await tagApi.delete(id); load() } }
function startEdit(t: Tag) { editing.value = t.id; editName.value = t.name }

onMounted(load)
</script>

<template>
  <div class="max-w-6xl mx-auto px-4 py-8">
    <div class="flex gap-8">
      <AdminSidebar />
      <div class="flex-1 min-w-0">
        <h1 class="text-2xl font-black mb-6" style="font-family: var(--font-display)">标签管理</h1>
        <div class="flex gap-2 mb-4">
          <input v-model="newName" @keyup.enter="create" placeholder="新标签名称"
                 class="px-3 py-2 rounded-lg text-sm border outline-none flex-1"
                 style="border-color: var(--border-color); background-color: var(--bg-card); color: var(--text-primary)" />
          <button @click="create" class="px-4 py-2 rounded-lg text-sm font-bold text-white transition-all hover:scale-105"
                  style="background: var(--accent-pink)">添加</button>
        </div>
        <div class="flex flex-wrap gap-2">
          <div v-for="tag in tags" :key="tag.id" class="flex items-center gap-1 px-3 py-1.5 rounded-full text-sm"
               style="background-color: var(--bg-card); border: 1px solid var(--border-color)">
            <template v-if="editing === tag.id">
              <input v-model="editName" @keyup.enter="update(tag.id)" class="w-20 px-1 text-sm border outline-none rounded"
                     style="border-color: var(--border-color); background-color: var(--bg-secondary); color: var(--text-primary)" />
              <button @click="update(tag.id)" class="text-xs" style="color: green">✓</button>
              <button @click="editing = null" class="text-xs" style="color: var(--text-secondary)">✕</button>
            </template>
            <template v-else>
              <span style="color: var(--text-primary)">{{ tag.name }}</span>
              <button @click="startEdit(tag)" class="text-xs ml-1" style="color: var(--accent-pink)">✎</button>
              <button @click="remove(tag.id)" class="text-xs" style="color: var(--accent-red)">✕</button>
            </template>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
