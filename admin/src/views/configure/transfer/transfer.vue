<template>
    <div class="deposit-list">
        <!-- Navbar -->
        <el-card class="!border-none" shadow="never">
            <div v-if="currencies.length > 0">
                <h2 class="font-bold text-2xl text-center flex justify-center items-center mb-2">Select a currency</h2>
                <hr class="opacity-60 mb-2">
                <div class="grid grid-cols-3 gap-4">
                    <div v-for="currency in currencies" :key="currency.currencyId"
                        class="m-2 border rounded p-3 ease-in-out duration-300 cursor-pointer hover:-translate-y-2 text-center"
                        @mouseover="highlightCurrency(currency)" @mouseleave="unhighlightCurrency(currency)"
                        @click="handleEdit(currency)" :class="{ 'bg-gray-200': currency.highlighted }">

                        <span>
                            {{ currency.currencyName }} ({{ currency.currencyCode }}) - {{ currency.symbol }}
                        </span>

                    </div>
                </div>
            </div>
            <div v-else>
                <p>Loading...</p>
            </div>
        </el-card>
    </div>
    <edit-popup v-if="showEdit" ref="editRef" @success="fetchData" @close="showEdit = false" />
</template>

<script setup lang="ts">
import { ref, onActivated } from 'vue';
import { configureCurrencyList } from '@/api/configure';
import editPopup from "./edit.vue";

const editRef = shallowRef<InstanceType<typeof editPopup>>()
const showEdit = ref(false)

const currencies = ref([]);
const selectedCurrency = ref(null); // Seçilen currency'i depolamak için bir ref ekledik

const handleEdit = async (data: any) => {
    showEdit.value = true
    await nextTick()
    editRef.value?.open()
    editRef.value?.getDetail(data)
}


const fetchData = async () => {
    try {
        const result = await configureCurrencyList();
        currencies.value = result.map((currency) => ({
            ...currency,
            highlighted: false,
        }));
        console.log(result);
    } catch (error) {
        console.error('Veri alınırken hata oluştu:', error);
    }
};

const highlightCurrency = (currency) => {
    currency.highlighted = true;
};

const unhighlightCurrency = (currency) => {
    currency.highlighted = false;
};

const selectCurrency = (currency) => {
    console.log(currency);

    selectedCurrency.value = currency;
};

onActivated(() => {
    fetchData();
});

fetchData();
</script>

<style scoped>
/* Burada gerekirse özel CSS stilleri ekleyebilirsiniz */
</style>
