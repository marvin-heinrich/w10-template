<script lang="ts">
    import Icon from "@iconify/svelte";

    interface DietaryFilters {
        vegetarian: boolean;
        vegan: boolean;
        containsMeat: boolean;
    }

    let {
        searchQuery = $bindable(),
        dietaryFilters = $bindable(),
        showFilterPanel = $bindable()
    }: {
        searchQuery: string;
        dietaryFilters: DietaryFilters;
        showFilterPanel: boolean;
    } = $props();

    function resetFilters() {
        dietaryFilters = {
            vegetarian: false,
            vegan: false,
            containsMeat: false,
        };
    }
</script>

<div class="search-container">
    <div class="search-bar">
        <input
            type="text"
            placeholder="Search meals..."
            bind:value={searchQuery}
        />
        <button
            class="filter-button"
            onclick={() => (showFilterPanel = !showFilterPanel)}
            aria-label="Filter"
        >
            <Icon icon="mdi:filter" width="20" height="20" />
        </button>
        <button class="search-button" aria-label="Search">
            <Icon icon="mdi:search" width="20" height="20" />
        </button>

        {#if showFilterPanel}
            <div class="filter-panel">
                <div class="filter-header">
                    <h3>
                        <Icon
                            icon="mdi:tune-vertical"
                            width="18"
                            height="18"
                        />
                        Filter Options
                    </h3>
                    <button
                        class="close-button"
                        onclick={() => (showFilterPanel = false)}
                    >
                        <Icon icon="mdi:close" width="20" height="20" />
                    </button>
                </div>

                <div class="filter-section">
                    <h4>
                        <Icon
                            icon="mdi:food-apple-outline"
                            class="section-icon"
                            width="16"
                            height="16"
                        />
                        Dietary Preferences
                    </h4>
                    <div class="filter-options-grid">
                        <label class="custom-checkbox">
                            <input
                                type="checkbox"
                                bind:checked={dietaryFilters.vegetarian}
                            />
                            <span class="checkmark"></span>
                            <div class="option-label">
                                <Icon
                                    icon="mdi:leaf"
                                    class="filter-icon"
                                    width="16"
                                    height="16"
                                />
                                <span>Vegetarian</span>
                            </div>
                        </label>
                        <label class="custom-checkbox">
                            <input
                                type="checkbox"
                                bind:checked={dietaryFilters.vegan}
                            />
                            <span class="checkmark"></span>
                            <div class="option-label">
                                <Icon
                                    icon="mdi:sprout"
                                    class="filter-icon"
                                    width="16"
                                    height="16"
                                />
                                <span>Vegan</span>
                            </div>
                        </label>
                        <label class="custom-checkbox">
                            <input
                                type="checkbox"
                                bind:checked={dietaryFilters.containsMeat}
                            />
                            <span class="checkmark"></span>
                            <div class="option-label">
                                <Icon
                                    icon="mdi:food-steak"
                                    class="filter-icon"
                                    width="16"
                                    height="16"
                                />
                                <span>Contains Meat</span>
                            </div>
                        </label>
                    </div>
                </div>

                <div class="filter-actions">
                    <button
                        class="reset-button"
                        onclick={resetFilters}
                    >
                        <Icon icon="mdi:refresh" width="16" height="16" />
                        Reset
                    </button>
                </div>
            </div>
        {/if}
    </div>
</div>
