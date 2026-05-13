<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { trackApi, type Track } from '../../api/track'
import AdminSidebar from '../../components/layout/AdminSidebar.vue'

const tracks = ref<Track[]>([])
const showForm = ref(false)
const editingId = ref<number | null>(null)
const form = ref({ title: '', artist: '', url: '', cover: '', duration: 240, sortOrder: 0 })

function resetForm() {
  form.value = { title: '', artist: '', url: '', cover: '', duration: 240, sortOrder: 0 }
  editingId.value = null
}

function openCreate() { resetForm(); showForm.value = true }
function openEdit(t: Track) {
  form.value = { title: t.title, artist: t.artist || '', url: t.url || '', cover: t.cover || '', duration: t.duration || 240, sortOrder: t.sortOrder }
  editingId.value = t.id
  showForm.value = true
}
function cancel() { showForm.value = false; resetForm() }

async function load() { const r = await trackApi.listAdmin(); tracks.value = r.data.data }
async function save() {
  if (!form.value.title.trim()) return
  if (editingId.value) {
    await trackApi.update(editingId.value, form.value)
  } else {
    await trackApi.create(form.value)
  }
  showForm.value = false
  resetForm()
  load()
}
async function remove(id: number) { if (confirm('确定删除？')) { await trackApi.delete(id); load() } }

onMounted(load)
</script>

<template>
  <div class="max-w-6xl mx-auto px-4 py-8">
    <div class="flex gap-8">
      <AdminSidebar />
      <div class="flex-1 min-w-0">
        <div class="flex items-center justify-between mb-6">
          <h1 class="text-2xl font-black" style="font-family: var(--font-display)">音乐管理</h1>
          <button @click="openCreate" class="px-4 py-2 rounded-lg text-sm font-bold text-white transition-all hover:scale-105"
                  style="background: var(--accent-pink)">新增曲目</button>
        </div>

        <div v-if="showForm" class="fixed inset-0 z-50 flex items-center justify-center" style="background: rgba(0,0,0,0.5)" @click.self="cancel">
          <div class="w-full max-w-lg p-6 rounded-xl mx-4" style="background-color: var(--bg-card); border: 2px solid var(--ink); max-height: 90vh; overflow-y: auto">
            <h2 class="text-lg font-bold mb-4" style="font-family: var(--font-display)">{{ editingId ? '编辑' : '新增' }}曲目</h2>
            <div class="space-y-3">
              <div>
                <label class="block text-xs font-bold mb-1" style="color: var(--text-secondary)">标题</label>
                <input v-model="form.title" class="w-full px-3 py-2 rounded-lg text-sm border outline-none"
                       style="border-color: var(--border-color); background-color: var(--bg-secondary); color: var(--text-primary)" />
              </div>
              <div class="grid grid-cols-2 gap-3">
                <div>
                  <label class="block text-xs font-bold mb-1" style="color: var(--text-secondary)">艺术家</label>
                  <input v-model="form.artist" class="w-full px-3 py-2 rounded-lg text-sm border outline-none"
                         style="border-color: var(--border-color); background-color: var(--bg-secondary); color: var(--text-primary)" />
                </div>
                <div>
                  <label class="block text-xs font-bold mb-1" style="color: var(--text-secondary)">时长(秒)</label>
                  <input v-model.number="form.duration" type="number" class="w-full px-3 py-2 rounded-lg text-sm border outline-none"
                         style="border-color: var(--border-color); background-color: var(--bg-secondary); color: var(--text-primary)" />
                </div>
              </div>
              <div>
                <label class="block text-xs font-bold mb-1" style="color: var(--text-secondary)">音频 URL</label>
                <input v-model="form.url" placeholder="留空则使用模拟播放" class="w-full px-3 py-2 rounded-lg text-sm border outline-none"
                       style="border-color: var(--border-color); background-color: var(--bg-secondary); color: var(--text-primary)" />
              </div>
              <div>
                <label class="block text-xs font-bold mb-1" style="color: var(--text-secondary)">封面 URL</label>
                <input v-model="form.cover" class="w-full px-3 py-2 rounded-lg text-sm border outline-none"
                       style="border-color: var(--border-color); background-color: var(--bg-secondary); color: var(--text-primary)" />
              </div>
              <div>
                <label class="block text-xs font-bold mb-1" style="color: var(--text-secondary)">排序</label>
                <input v-model.number="form.sortOrder" type="number" class="w-full px-3 py-2 rounded-lg text-sm border outline-none"
                       style="border-color: var(--border-color); background-color: var(--bg-secondary); color: var(--text-primary)" />
              </div>
            </div>
            <div class="flex gap-2 mt-4 justify-end">
              <button @click="cancel" class="px-4 py-2 rounded-lg text-sm"
                      style="background-color: var(--bg-secondary); color: var(--text-secondary); border: 1px solid var(--border-color)">取消</button>
              <button @click="save" class="px-4 py-2 rounded-lg text-sm font-bold text-white" style="background: var(--accent-pink)">保存</button>
            </div>
          </div>
        </div>

        <div class="overflow-x-auto">
          <table class="w-full text-sm" style="border-collapse: collapse">
            <thead>
              <tr style="border-bottom: 2px solid var(--ink)">
                <th class="text-left py-2 px-2" style="color: var(--text-secondary); font-weight: 700; font-size: 0.7rem;">标题</th>
                <th class="text-left py-2 px-2" style="color: var(--text-secondary); font-weight: 700; font-size: 0.7rem;">艺术家</th>
                <th class="text-left py-2 px-2" style="color: var(--text-secondary); font-weight: 700; font-size: 0.7rem;">时长</th>
                <th class="text-left py-2 px-2" style="color: var(--text-secondary); font-weight: 700; font-size: 0.7rem;">排序</th>
                <th class="text-right py-2 px-2" style="color: var(--text-secondary); font-weight: 700; font-size: 0.7rem;">操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="t in tracks" :key="t.id" class="align-middle" style="border-bottom: 1px solid var(--border-color)">
                <td class="py-2 px-2" style="color: var(--text-primary); font-weight: 600;">{{ t.title }}</td>
                <td class="py-2 px-2" style="color: var(--text-secondary)">{{ t.artist }}</td>
                <td class="py-2 px-2" style="color: var(--text-secondary)">{{ t.duration }}s</td>
                <td class="py-2 px-2" style="color: var(--text-secondary)">{{ t.sortOrder }}</td>
                <td class="py-2 px-2 text-right">
                  <button @click="openEdit(t)" class="text-xs px-2 py-1 rounded mr-1" style="color: var(--accent-pink)">编辑</button>
                  <button @click="remove(t.id)" class="text-xs px-2 py-1 rounded" style="color: var(--accent-red)">删除</button>
                </td>
              </tr>
              <tr v-if="tracks.length === 0">
                <td colspan="5" class="py-8 text-center" style="color: var(--text-secondary)">暂无曲目，点击右上角新增</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>
