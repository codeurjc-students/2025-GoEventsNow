export function getSelectedFile(event: any): File | null {
    return event.target.files[0] ?? null;
}
