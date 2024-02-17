<template>
    <div>
        <el-card class="!border-none" shadow="never">
            <el-form ref="formRef" class="mb-[-16px]" :model="queryParams" :inline="true">
                <el-form-item>
                    <el-select v-model="selectedField">
                        <el-option label="Transaction Number" value="transactionNumber" />
                        <el-option label="Iban (TL)" value="iban" />
                        <el-option label="User Id" value="payId" />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-input class="w-[280px]" v-model="queryParams[selectedField]" clearable @keyup.enter="resetPage" />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="resetPage">Search</el-button>
                    <el-button @click="resetParams">Reset</el-button>
                </el-form-item>

            </el-form>

        </el-card>
        <el-card class="!border-none mt-4" shadow="never">
            <el-form-item class="flex justify-self-end">
                <el-button type="primary" @click="exportSelected">Export Selected</el-button>
            </el-form-item>
            <el-table v-loading="pager.loading" :data="pager.lists" @selection-change="handleSelectionChange">


                <el-table-column type="selection" width="55" align="center"></el-table-column>

                <el-table-column label="Transaction Number" prop="transactionNumber" min-width="160" show-overflow-tooltip>
                    <template #default="{ row }">
                        <span style="border: 2px; padding: 2px;">
                            <el-tooltip class="box-item" effect="dark" :content="row.transactionNumber" placement="top">
                                {{ formatTransactionNumber(row.transactionNumber) }}
                            </el-tooltip>
                        </span>
                    </template>
                </el-table-column>
                <el-table-column label="IBAN(TR)" prop="iban" min-width="220" />
                <el-table-column label="Bank Name" prop="bankName" min-width="120" />
                <el-table-column label="Amount (TL)" prop="amount" min-width="140" />
                <el-table-column label="Real Amount (TL)" prop="realAmount" min-width="160" />

                <el-table-column label="status" min-width="160">
                    <template #header>
                        <el-form :model="queryParams" :inline="true" class="mt-4">
                            <el-form-item>
                                <el-select v-model="statusFilter" placeholder="Status" @change="handleStatusChange">
                                    <el-option label="All" value=""></el-option>
                                    <el-option label="Success" value="1" />
                                    <el-option label="Failed" value="-1" />
                                    <el-option label="Pending" value="2" />
                                </el-select>
                            </el-form-item>
                        </el-form>

                    </template>
                    <template #default="{ row }">
                        <span :style="row.status === 1 ? 'color: green' : row.status === 2 ? 'color: orange' : 'color: red'"
                            style="border: 2px; padding: 2px;">
                            {{ row.statusMessage }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column label="User Id" prop="payId" min-width="160" />
                <el-table-column label="Creation Time" min-width="200">
                    <template #default="{ row }">
                        <span style="border: 2px; padding: 2px;">
                            {{ formatTimeIfCurrentYear(row.createTime) }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column label="Complete Time" min-width="200">
                    <template #default="{ row }">
                        <span style="border: 2px; padding: 2px;">
                            {{ formatTimeIfCurrentYear(row.completeTime) }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column label="Action" width="140" fixed="right">
                    <template #default="{ row }">

                        <el-button type="primary" link @click="handleVerify(row.uuid)"
                            :style="row.status === 2 ? { display: '' } : { display: 'none' }">
                            verify
                        </el-button>

                        <el-button type="primary" link @click="handleDetail(row.uuid)">
                            detail
                        </el-button>

                    </template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>
        <detail-popup v-if="showDetail" ref="detailRef" @success="getLists" @close="showDetail = false" />
        <verify-popup v-if="showDetail" ref="verifyRef" @success="getLists" @close="showDetail = false" />

    </div>
</template>
<script lang="ts" setup name="withdrawList">
import { withdrawList } from '@/api/withdraw'
import { useDictOptions } from '@/hooks/useDictOptions'
import { usePaging } from '@/hooks/usePaging'
import detailPopup from "./detail.vue";
import verifyPopup from "./verify.vue";
import { formatTransactionNumber, formatTimeIfCurrentYear } from '@/utils/filters';

const selectedField = ref('transactionNumber'); // Başlangıçta varsayılan olarak "Transaction Number" seçili olsun

const detailRef = shallowRef<InstanceType<typeof detailPopup>>()
const verifyRef = shallowRef<InstanceType<typeof verifyPopup>>()
const showDetail = ref(false)
const statusFilter = ref(''); // Initialize with an empty string


const handleDetail = async (data: any) => {
    showDetail.value = true
    await nextTick()
    detailRef.value?.open()
    detailRef.value?.getDetail(data)
}

const handleVerify = async (data: any) => {
    showDetail.value = true
    await nextTick()
    verifyRef.value?.open()
    verifyRef.value?.getDetail(data)

}


const selectedRows = ref([]);


const handleSelectionChange = (rows: any[]) => {
    selectedRows.value = rows;
};

const exportSelected = () => {
    // Here, you can use a library like 'xlsx' to create an Excel file.
    // For simplicity, let's assume each row is an object with properties.
    const selectedData = selectedRows.value.map(row => {
        return {
            transactionNumber: row.transactionNumber,
            iban: row.iban,
            bankName: row.bankName,
            payId: row.payId,
            amount: row.amount,
            realAmount: row.realAmount,
            statusMSG: row.statusMSG,
            createTime: row.createTime,
            completeTime: row.completeTime,
        };
    });

    // Example using 'xlsx' library
    import('xlsx').then(xlsx => {
        const ws = xlsx.utils.json_to_sheet(selectedData);
        const wb = xlsx.utils.book_new();
        xlsx.utils.book_append_sheet(wb, ws, 'Selected Data');
        xlsx.writeFile(wb, 'selected_data.xlsx');
    });
};


const queryParams = reactive({
    transactionNumber: '',
    payId: '',
    Iban: '',
    status: ''
})


const handleStatusChange = () => {

    queryParams.status = statusFilter.value;

    resetPage();
};

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: withdrawList,
    params: queryParams
})

const { optionsData } = useDictOptions<{
    withdrawlist: any[]
}>({
    withdrawlist: {
        api: withdrawList
    }
})


onActivated(() => {
    getLists()
})

getLists()
</script>
