<template>
    <div class="article-lists">
        <el-card class="!border-none" shadow="never">
            <el-form ref="formRef" class="mb-[-16px]" :model="queryParams" :inline="true">
                <el-form-item>
                    <el-select v-model="selectedField">
                        <el-option label="Transaction Number" value="transactionNumber" />
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
            <el-table v-loading="pager.loading" :data="pager.lists">
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
                <el-table-column label="Currency" prop="currency" min-width="100" />
                <el-table-column label="Amount (TL)" prop="amount" min-width="140" />
                <el-table-column label="Real Amount (TL)" prop="realAmount" min-width="160" />
                <el-table-column label="User Id" prop="payId" min-width="160" />

                <el-table-column label="status" min-width="160">
                    <template #header>
                        <el-form :model="queryParams" :inline="true" class="mt-4">
                            <el-form-item>
                                <el-select v-model="statusFilter" placeholder="Status" @change="handleFilterChange">
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
                            {{ row.statusMSG }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column label="typeDetail" min-width="160">
                    <template #header>
                        <el-form :model="queryParams" :inline="true" class="mt-4">
                            <el-form-item>
                                <el-select v-model="typeFilter" placeholder="Type" @change="handleFilterChange">
                                    <el-option label="All" value=""></el-option>
                                    <el-option label="Deposit" value="1"></el-option>
                                    <el-option label="Withdrawal" value="2"></el-option>
                                    <el-option label="Transfer Deposit" value="3"></el-option>
                                    <el-option label="Transfer Withdrawal" value="4"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-form>
                    </template>
                    <template #default="{ row }">
                        {{ row.typeMSG }}
                    </template>
                </el-table-column>
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
                <el-table-column label="Action" width="120" fixed="right">
                    <template #default="{ row }">
                        <el-button type="primary" link @click="handleDetail(row.detailId, row.typeDetail)">
                            detail
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>
        <deposit-popup v-if="showDetail" ref="depositPopupRef" @success="getLists" @close="showDetail = false" />
        <transfer-popup v-if="showDetail" ref="transferPopupRef" @success="getLists" @close="showDetail = false" />
        <withdrawal-popup v-if="showDetail" ref="withdrawalPopupRef" @success="getLists" @close="showDetail = false" />

    </div>
</template>
<script lang="ts" setup name="articleLists">
import { transactionList } from '@/api/transaction'
import { usePaging } from '@/hooks/usePaging'
import depositPopup from '@/views/deposit/detail.vue'
import transferPopup from '@/views/transfer/detail.vue'
import withdrawalPopup from '@/views/withdrawal/detail.vue'
import { formatTransactionNumber, formatTimeIfCurrentYear } from '@/utils/filters';


const selectedField = ref('transactionNumber');
const selectedRows = ref([]);

const depositPopupRef = shallowRef<InstanceType<typeof depositPopup>>();
const transferPopupRef = shallowRef<InstanceType<typeof transferPopup>>();
const withdrawalPopupRef = shallowRef<InstanceType<typeof withdrawalPopup>>();

const showDetail = ref(false)
const statusFilter = ref('');
const typeFilter = ref('');


const handleDetail = async (detailId: any, typeDetail: any) => {
    showDetail.value = true;
    await nextTick();

    if (typeDetail === 1) {
        // Deposit
        depositPopupRef.value?.open();
        depositPopupRef.value?.getDetail(detailId);
    } else if (typeDetail === 2) {
        // Transfer
        withdrawalPopupRef.value?.open();
        withdrawalPopupRef.value?.getDetail(detailId);
    }
    else if (typeDetail === 3 || typeDetail == 4) {
        // Transfer
        transferPopupRef.value?.open();
        transferPopupRef.value?.getDetail(detailId);
    }
}

const handleFilterChange = () => {
    queryParams.status = statusFilter.value;
    queryParams.typeDetail = typeFilter.value;
    resetPage();
};

const exportSelected = () => {


    const selectedData = selectedRows.value.map(row => {
        return {
            transactionNumber: row.transactionNumber,
            currency: row.currency,
            payId: row.payId,
            amount: row.amount,
            realAmount: row.realAmount,
            typeMSG: row.typeMSG,
            statusMSG: row.statusMSG,
            createTime: row.createTime,
            completeTime: row.completeTime,
        };
    });


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
    status: '',
    typeDetail: '',
})



const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: transactionList,
    params: queryParams
})


onActivated(() => {
    getLists()
})

getLists()
</script>
