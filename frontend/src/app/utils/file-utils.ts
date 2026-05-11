export function getSelectedFile(event: any): File | null {
    const file = event.target.files[0];
    return file ? file:null;
}
