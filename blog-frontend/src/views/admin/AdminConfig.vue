<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { siteConfigApi, type SiteConfig } from '../../api/siteConfig'
import AdminSidebar from '../../components/layout/AdminSidebar.vue'

const configs = ref<SiteConfig[]>([])
const showForm = ref(false)
const editingId = ref<number | null>(null)
const form = ref({ configKey: '', configValue: '' })

function resetForm() {
  form.value = { configKey: '', configValue: '' }
  editingId.value = null
}

function openCreate() { resetForm(); showForm.value = true }
function openEdit(c: SiteConfig) {
  form.value = { configKey: c.configKey, configValue: c.configValue || '' }
  editingId.value = c.id
  showForm.value = true
}
function cancel() { showForm.value = false; resetForm() }

async function load() { const r = await siteConfigApi.listAdmin(); configs.value = r.data.data }
async function save() {
  if (!form.value.configKey.trim()) return
  if (editingId.value) {
    await siteConfigApi.update(editingId.value, { configKey: form.value.configKey, configValue: form.value.configValue })
  } else {
    await siteConfigApi.create(form.value)
  }
  showForm.value = false
  resetForm()
  load()
}
async function remove(id: number) { if (confirm('确定删除？')) { await siteConfigApi.delete(id); load() } }

onMounted(load)
</script>

<template>
  <div class="max-w-6xl mx-auto px-4 py-8">
    <div class="flex gap-8">
      <AdminSidebar />
      <div class="flex-1 min-w-0">
        <div class="flex items-center justify-between mb-6">
          <h1 class="text-2xl font-black" style="font-family: var(--font-display)">站点配置</h1>
          <button @click="openCreate" class="px-4 py-2 rounded-lg text-sm font-bold text-white transition-all hover:scale-105"
                  style="background: var(--accent-pink)">新增配置</button>
        </div>

        <div v-if="showForm" class="fixed inset-0 z-50 flex items-center justify-center" style="background: rgba(0,0,0,0.5)" @click.self="cancel">
          <div class="w-full max-w-lg p-6 rounded-xl mx-4" style="background-color: var(--bg-card); border: 2px solid var(--ink); max-height: 90vh; overflow-y: auto">
            <h2 class="text-lg font-bold mb-4" style="font-family: var(--font-display)">{{ editingId ? '编辑' : '新增' }}配置</h2>
            <div class="space-y-3">
              <div>
                <label class="block text-xs font-bold mb-1" style="color: var(--text-secondary)">配置键</label>
                <input v-model="form.configKey" :disabled="!!editingId" placeholder="about_title" class="w-full px-3 py-2 rounded-lg text-sm border outline-none"
                       style="border-color: var(--border-color); background-color: var(--bg-secondary); color: var(--text-primary)" />
              </div>
              <div>
                <label class="block text-xs font-bold mb-1" style="color: var(--text-secondary)">配置值</label>
                <textarea v-model="form.configValue" rows="4" class="w-full px-3 py-2 rounded-lg text-sm border outline-none resize-none"
                          style="border-color: var(--border-color); background-color: var(--bg-secondary); color: var(--text-primary)"></textarea>
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
                <th class="text-left py-2 px-2" style="color: var(--text-secondary); font-weight: 700; font-size: 0.7rem;">配置键</th>
                <th class="text-left py-2 px-2" style="color: var(--text-secondary); font-weight: 700; font-size: 0.7rem;">配置值</th>
                <th class="text-right py-2 px-2" style="color: var(--text-secondary); font-weight: 700; font-size: 0.7rem;">操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="c in configs" :key="c.id" class="align-middle" style="border-bottom: 1px solid var(--border-color)">
                <td class="py-2 px-2" style="color: var(--text-primary); font-weight: 600; font-family: var(--font-mono); font-size: 0.75rem;">{{ c.configKey }}</td>
                <td class="py-2 px-2" style="color: var(--text-secondary); max-width: 300px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">{{ c.configValue }}</td>
                <td class="py-2 px-2 text-right">
                  <button @click="openEdit(c)" class="text-xs px-2 py-1 rounded mr-1" style="color: var(--accent-pink)">编辑</button>
                  <button @click="remove(c.id)" class="text-xs px-2 py-1 rounded" style="color: var(--accent-red)">删除</button>
                </td>
              </tr>
              <tr v-if="configs.length === 0">
                <td colspan="3" class="py-8 text-center" style="color: var(--text-secondary)">暂无配置，点击右上角新增</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>
