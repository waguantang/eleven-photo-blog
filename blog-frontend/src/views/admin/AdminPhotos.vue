<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { photoApi, type Photo } from '../../api/photo'
import AdminSidebar from '../../components/layout/AdminSidebar.vue'

const photos = ref<Photo[]>([])
const showForm = ref(false)
const editingId = ref<number | null>(null)
const form = ref({ title: '', description: '', src: '', category: '', sortOrder: 0 })

function resetForm() {
  form.value = { title: '', description: '', src: '', category: '', sortOrder: 0 }
  editingId.value = null
}

function openCreate() { resetForm(); showForm.value = true }
function openEdit(p: Photo) {
  form.value = { title: p.title, description: p.description || '', src: p.src, category: p.category || '', sortOrder: p.sortOrder }
  editingId.value = p.id
  showForm.value = true
}
function cancel() { showForm.value = false; resetForm() }

async function load() { const r = await photoApi.listAdmin(); photos.value = r.data.data }
async function save() {
  if (!form.value.title.trim() || !form.value.src.trim()) return
  if (editingId.value) {
    await photoApi.update(editingId.value, form.value)
  } else {
    await photoApi.create(form.value)
  }
  showForm.value = false
  resetForm()
  load()
}
async function remove(id: number) { if (confirm('确定删除？')) { await photoApi.delete(id); load() } }

onMounted(load)
</script>

<template>
  <div class="max-w-6xl mx-auto px-4 py-8">
    <div class="flex gap-8">
      <AdminSidebar />
      <div class="flex-1 min-w-0">
        <div class="flex items-center justify-between mb-6">
          <h1 class="text-2xl font-black" style="font-family: var(--font-display)">作品管理</h1>
          <button @click="openCreate" class="px-4 py-2 rounded-lg text-sm font-bold text-white transition-all hover:scale-105"
                  style="background: var(--accent-pink)">新增作品</button>
        </div>

        <!-- Form modal -->
        <div v-if="showForm" class="fixed inset-0 z-50 flex items-center justify-center" style="background: rgba(0,0,0,0.5)" @click.self="cancel">
          <div class="w-full max-w-lg p-6 rounded-xl mx-4" style="background-color: var(--bg-card); border: 2px solid var(--ink); max-height: 90vh; overflow-y: auto">
            <h2 class="text-lg font-bold mb-4" style="font-family: var(--font-display)">{{ editingId ? '编辑' : '新增' }}作品</h2>
            <div class="space-y-3">
              <div>
                <label class="block text-xs font-bold mb-1" style="color: var(--text-secondary)">标题</label>
                <input v-model="form.title" class="w-full px-3 py-2 rounded-lg text-sm border outline-none"
                       style="border-color: var(--border-color); background-color: var(--bg-secondary); color: var(--text-primary)" />
              </div>
              <div>
                <label class="block text-xs font-bold mb-1" style="color: var(--text-secondary)">图片路径</label>
                <input v-model="form.src" placeholder="/YouName/xxx.jpg" class="w-full px-3 py-2 rounded-lg text-sm border outline-none"
                       style="border-color: var(--border-color); background-color: var(--bg-secondary); color: var(--text-primary)" />
              </div>
              <div>
                <label class="block text-xs font-bold mb-1" style="color: var(--text-secondary)">描述</label>
                <textarea v-model="form.description" rows="3" class="w-full px-3 py-2 rounded-lg text-sm border outline-none resize-none"
                          style="border-color: var(--border-color); background-color: var(--bg-secondary); color: var(--text-primary)"></textarea>
              </div>
              <div class="grid grid-cols-2 gap-3">
                <div>
                  <label class="block text-xs font-bold mb-1" style="color: var(--text-secondary)">分类</label>
                  <input v-model="form.category" placeholder="风光/街拍/人像..." class="w-full px-3 py-2 rounded-lg text-sm border outline-none"
                         style="border-color: var(--border-color); background-color: var(--bg-secondary); color: var(--text-primary)" />
                </div>
                <div>
                  <label class="block text-xs font-bold mb-1" style="color: var(--text-secondary)">排序</label>
                  <input v-model.number="form.sortOrder" type="number" class="w-full px-3 py-2 rounded-lg text-sm border outline-none"
                         style="border-color: var(--border-color); background-color: var(--bg-secondary); color: var(--text-primary)" />
                </div>
              </div>
            </div>
            <div class="flex gap-2 mt-4 justify-end">
              <button @click="cancel" class="px-4 py-2 rounded-lg text-sm"
                      style="background-color: var(--bg-secondary); color: var(--text-secondary); border: 1px solid var(--border-color)">取消</button>
              <button @click="save" class="px-4 py-2 rounded-lg text-sm font-bold text-white" style="background: var(--accent-pink)">保存</button>
            </div>
          </div>
        </div>

        <!-- Table -->
        <div class="overflow-x-auto">
          <table class="w-full text-sm" style="border-collapse: collapse">
            <thead>
              <tr style="border-bottom: 2px solid var(--ink)">
                <th class="text-left py-2 px-2" style="color: var(--text-secondary); font-weight: 700; font-size: 0.7rem;">预览</th>
                <th class="text-left py-2 px-2" style="color: var(--text-secondary); font-weight: 700; font-size: 0.7rem;">标题</th>
                <th class="text-left py-2 px-2" style="color: var(--text-secondary); font-weight: 700; font-size: 0.7rem;">分类</th>
                <th class="text-left py-2 px-2" style="color: var(--text-secondary); font-weight: 700; font-size: 0.7rem;">排序</th>
                <th class="text-right py-2 px-2" style="color: var(--text-secondary); font-weight: 700; font-size: 0.7rem;">操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="p in photos" :key="p.id" class="align-middle" style="border-bottom: 1px solid var(--border-color)">
                <td class="py-2 px-2">
                  <img :src="p.src" :alt="p.title" class="w-16 h-12 object-cover rounded" style="border: 1px solid var(--border-color)" />
                </td>
                <td class="py-2 px-2" style="color: var(--text-primary); font-weight: 600;">{{ p.title }}</td>
                <td class="py-2 px-2">
                  <span v-if="p.category" class="px-2 py-0.5 rounded-full text-xs"
                        style="background-color: var(--bg-secondary); color: var(--text-secondary); border: 1px solid var(--border-color)">{{ p.category }}</span>
                </td>
                <td class="py-2 px-2" style="color: var(--text-secondary)">{{ p.sortOrder }}</td>
                <td class="py-2 px-2 text-right">
                  <button @click="openEdit(p)" class="text-xs px-2 py-1 rounded mr-1" style="color: var(--accent-pink)">编辑</button>
                  <button @click="remove(p.id)" class="text-xs px-2 py-1 rounded" style="color: var(--accent-red)">删除</button>
                </td>
              </tr>
              <tr v-if="photos.length === 0">
                <td colspan="5" class="py-8 text-center" style="color: var(--text-secondary)">暂无作品，点击右上角新增</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>
