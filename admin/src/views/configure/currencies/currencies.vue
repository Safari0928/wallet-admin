<template>
    <div class="deposit-list">
        <!-- Navbar -->
        <el-card class="!border-none" shadow="never">
            <div v-if="currencies.length > 0">
                <ul>
                    <h2 class="font-bold text-2xl">Edit currencies</h2>
                    <li v-for="currency in currencies" :key="currency.currencyId">
                        <div
                            class="m-2 border rounded p-2 hover:py-4 ease-in-out duration-300 cursor-pointer flex justify-between">
                            <span>
                                {{ currency.currencyName }} ({{ currency.currencyCode }}) - {{ currency.symbol }}
                            </span>
                            <span class="flex gap-2">
                                <span>
                                    <div class="font-bold text-yellow-500 ">
                                        <el-tag :type="currency.operate === 1 ? 'success' : 'error'" class="ml-2">{{
                                            currency.operate === 1 ? 'enable' : 'disable' }}</el-tag>
                                    </div>

                                </span>
                                <el-button type="primary" link @click="handleEdit(currency)">
                                    edit
                                </el-button>
                            </span>
                        </div>
                    </li>
                </ul>
            </div>
            <div v-else>
                <p>Loading...</p>
            </div>
        </el-card>
      <edit-popup v-if="showEdit" ref="editRef" @success="fetchData" @close="showEdit = false" />
        



    </div>
</template>
  
<script setup lang="ts">
import { ref, onActivated } from 'vue';
import { configureCurrencyList } from '@/api/configure';
import editPopup from "./edit.vue";


const currencies = ref([]);



const editRef = shallowRef<InstanceType<typeof editPopup>>()
const showEdit = ref(false)


const handleEdit = async (data: any) => {
  showEdit.value = true
  await nextTick()
  editRef.value?.open()
  editRef.value?.getDetail(data)
}


const fetchData = async () => {
    try {
        const result = await configureCurrencyList();
        currencies.value = result;
        console.log(result);

    } catch (error) {
        console.error('Veri alınırken hata oluştu:', error);
    }
};

onActivated(() => {
    fetchData();
});

fetchData();
</script>
  