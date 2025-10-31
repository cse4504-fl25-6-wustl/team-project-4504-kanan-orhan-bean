#!/bin/bash
set -euo pipefail

# Colors for readability
GREEN="\033[0;32m"
RED="\033[0;31m"
YELLOW="\033[1;33m"
NC="\033[0m" # No Color

process_csv_files() {
    local input_dir="$1"
    local client_config="$2"

    while IFS= read -r -d '' csv_file; do
        echo -e "\n=== Processing: ${YELLOW}$csv_file${NC} ==="

        # Get directory of the current test case
        local dir_name
        dir_name=$(dirname "$csv_file")

        # Where Gradle writes its output JSON
        local output_json="$CONFIG_DIR/Output.json"

        # Expected JSON for this test
        local expected_json="${dir_name}/expected_output.json"

        # 1️⃣ Run your application with this CSV as input
        echo "Running Java app..."
        gradle run --args="$csv_file $client_config $output_json" > /dev/null 2>&1

        # 2️⃣ Verify the output JSON was produced
        if [[ ! -s "$output_json" ]]; then
            echo -e "${RED}❌ No output produced for $csv_file${NC}"
            continue
        fi

        # 3️⃣ Verify expected output exists
        if [[ ! -f "$expected_json" ]]; then
            echo -e "${YELLOW}⚠️ Expected file not found:${NC} $expected_json"
            continue
        fi

        # 4️⃣ Compare actual vs expected JSON
        echo "Comparing output with expected..."
        python compare.py "$output_json" "$expected_json"
        compare_exit_code=$?

        # 5️⃣ Report result
        if [[ $compare_exit_code -eq 0 ]]; then
            echo -e "${GREEN}✅ Test passed for $csv_file${NC}"
        else
            echo -e "${RED}❌ Test failed for $csv_file (compare.py exit code: $compare_exit_code)${NC}"
        fi

        # Optional: cleanup output to avoid overwriting confusion
        rm -f "$output_json"

    done < <(find "$input_dir" -type f -name "*.csv" -print0)
}

# -------- CONFIG --------
INPUT_DIR="/Users/orhanerdogan/test_cases"
CONFIG_DIR="/Users/orhanerdogan/team-project-4504-kanan-orhan-bean/app/input"

NO_CRATES_CONFIG="$CONFIG_DIR/Site_requirements.csv"
ALLOW_CRATES_CONFIG="$CONFIG_DIR/Site_requirements_crate.csv"

# -------- RUN TESTS --------
echo -e "\n${YELLOW}=== Running box_packing tests ===${NC}"
process_csv_files "$INPUT_DIR/box_packing" "$NO_CRATES_CONFIG"

echo -e "\n${YELLOW}=== Running pallet_packing tests ===${NC}"
process_csv_files "$INPUT_DIR/pallet_packing" "$NO_CRATES_CONFIG"

echo -e "\n${YELLOW}=== Running crate_packing tests ===${NC}"
process_csv_files "$INPUT_DIR/crate_packing" "$ALLOW_CRATES_CONFIG"

echo -e "\n${GREEN}✅ All test batches complete.${NC}"
