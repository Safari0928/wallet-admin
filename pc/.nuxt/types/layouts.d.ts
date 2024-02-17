import { ComputedRef, Ref } from 'vue'
export type LayoutKey = "blank" | "default"
declare module "D:/workspace_project/likeadmin/pc/node_modules/nuxt/dist/pages/runtime/composables" {
  interface PageMeta {
    layout?: false | LayoutKey | Ref<LayoutKey> | ComputedRef<LayoutKey>
  }
}