

export function formatPhoneNumber(phoneNumber: string): string {
    const formattedNumber =
        phoneNumber.slice(0, 4) +
        ' ' +
        phoneNumber.slice(4, 7) +
        ' ' +
        phoneNumber.slice(7, 9) +
        ' ' +
        phoneNumber.slice(9, 11);

    return formattedNumber;
}

export function formatTransactionNumber(Number: string): string {
    const formattedNumber =
        Number.slice(0, 4) +
        ' ... ' +

        Number.slice(-4, -1);

    return formattedNumber;
}

export function formatName(Number: string): string {
    const formattedNumber =
        Number.slice(0, 2) +
        ' *** ' +

        Number.slice(-5, -1);

    return formattedNumber;
}

export function formatNumberWithTwoDecimalPlaces(number: number): string {
    if (isNaN(number)) {
        return "Invalid Number";
    }

    const integerPart = Math.floor(number);

    const decimalPart = number - integerPart;

    const truncatedDecimal = Math.floor(decimalPart * 100);

    const formattedNumber = integerPart + "." + truncatedDecimal.toString().padStart(2, "0");

    return formattedNumber;
}

export function formatTimeIfCurrentYear(dateString: string): string {
    const currentDate = new Date();
    const providedDate = new Date(dateString);

    //
    if (providedDate.getFullYear() === currentDate.getFullYear()) {
        const formattedTime =
            padZero(providedDate.getDate()) +
            ' ' +
            getMonthName(providedDate.getMonth()) +
            ', ' +
            formatNumberWithTwoDecimalPlaces(providedDate.getHours()) +
            ':' +
            formatNumberWithTwoDecimalPlaces(providedDate.getMinutes());

        return formattedTime;
    } else {
        
        const formattedFullDate =
            providedDate.getDate() +
            ' ' +
            getMonthName(providedDate.getMonth()) +
            ' ' +
            providedDate.getFullYear() +
            ', ' +
            formatNumberWithTwoDecimalPlaces(providedDate.getHours()) +
            ':' +
            formatNumberWithTwoDecimalPlaces(providedDate.getMinutes());

        return formattedFullDate;
    }
}



function padZero(num: number): string {
    return num.toString().padStart(2, '0');
}

function getMonthName(month: number): string {
    const monthNames = [
        'January', 'February', 'March', 'April', 'May', 'June',
        'July', 'August', 'September', 'October', 'November', 'December'
    ];
    return monthNames[month];
}