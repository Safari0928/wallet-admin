<template>
    <div class="user-list">
        <el-card class="!border-none" shadow="never">
            <el-form ref="formRef" class="mb-[-16px]" :model="queryParams" :inline="true">
                <el-form-item>
                    <el-select v-model="selectedField">
                        <el-option label="Phone Number" value="phoneNumber" />
                        <el-option label="email" value="email" />
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

            <el-table v-loading="pager.loading" :data="pager.lists">

                <el-table-column label="Phone Number" prop="phoneNumber" min-width="160" show-tooltip-when-overflow>
                    <template #default="{ row }">
                        <span style="border: 2px; padding: 2px;">
                            {{ formatPhoneNumber(row.phoneNumber) }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column label="country" min-width="160">
                    <template #header>
                        <el-form :model="queryParams" :inline="true" class="mt-4">
                            <el-form-item>
                                <el-select v-model="countryFilter" placeholder="Country" @change="handleStatusChange">
                                    <el-option label="All" value=""></el-option>
                                    <el-option v-for="country in countries" :key="country" :label="country"
                                        :value="country"></el-option>
                                </el-select>

                            </el-form-item>
                        </el-form>

                    </template>
                    <template #default="{ row }">
                        <span style="border: 2px; padding: 2px;">
                            {{ row.country }}
                        </span>
                    </template>
                </el-table-column>

                <el-table-column label="User Id" prop="payId" min-width="160" />
                <el-table-column label="Email" prop="email" min-width="160" />
                <el-table-column label="Verify" min-width="160">
                    <template #header>

                        <el-form :model="queryParams" :inline="true" class="mt-4">
                            <el-form-item>
                                <el-select v-model="verifyAccountFilter" placeholder="Verify" @change="handleStatusChange">
                                    <el-option label="All" value=""></el-option>
                                    <el-option label="Verified" value="1" />
                                    <el-option label="Unverified" value="-1" />
                                    <el-option label="Pending" value="2" />

                                </el-select>
                            </el-form-item>
                        </el-form>

                    </template>
                    <template #default="{ row }">
                        <span
                            :style="row.verifyAccount === 1 ? 'color: green' : row.verifyAccount === 2 ? 'color: orange' : 'color: red'"
                            style="border: 2px; padding: 2px;">
                            {{ row.verifyAccountMSG }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column label="Status" min-width="160">
                    <template #header>
                        <el-form :model="queryParams" :inline="true" class="mt-4">
                            <el-form-item>
                                <el-select v-model="statusFilter" placeholder="status" @change="handleStatusChange">
                                    <el-option label="All" value=""></el-option>
                                    <el-option label="Enable" value="1" />
                                    <el-option label="Disable" value="-1" />

                                </el-select>
                            </el-form-item>
                        </el-form>

                    </template>
                    <template #default="{ row }">
                        <span :style="row.status === -1 ? 'color: red' : ''" style="border: 2px; padding: 2px;">
                            {{ row.statusMSG }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column label="Creation Time" min-width="200">
                    <template #default="{ row }">
                        <span style="border: 2px; padding: 2px;">
                            {{ formatTimeIfCurrentYear(row.createTime) }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column label="Action" width="120" fixed="right">
                    <template #default="{ row }">
                        <router-link v-perms="['user:detail', 'user:detail']" :to="{
                            path: getRoutePath('user:detail'),
                            query: {
                                id: row.uuid
                            }

                        }">
                            <el-button>

                                detail
                            </el-button>
                        </router-link>

                    </template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>
    </div>
</template>
<script lang="ts" setup name="user">
import { userList, getUserCountries } from '@/api/user'
import { usePaging } from '@/hooks/usePaging'
import { getRoutePath } from '@/router'
import { formatPhoneNumber, formatTimeIfCurrentYear } from '@/utils/filters'


const selectedField = ref('phoneNumber');

const statusFilter = ref('');
const countryFilter = ref('');
const verifyAccountFilter = ref('');
const countries = ref([]);



const queryParams = reactive({
    phoneNumber: '',
    email: '',
    payId: '',
    status: "",
    country: "",
    verifyAccount: ""

})



const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: userList,
    params: queryParams
})


const handleStatusChange = () => {
    queryParams.status = statusFilter.value;
    queryParams.country = countryFilter.value;
    queryParams.verifyAccount = verifyAccountFilter.value;

    resetPage();
};

const getCountries = async () => {
    const data = await getUserCountries()
    countries.value = data


}

onActivated(() => {
    getLists()


})

getCountries()
getLists()

</script>
