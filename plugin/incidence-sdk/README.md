# incidence-sdk

Plugin in Ionic Capacitor to create a direct communication with Incidence.

## Install

```bash
npm install incidence-sdk
npx cap sync
```

## API

<docgen-index>

* [`initConfig(...)`](#initconfig)
* [`addDevice(...)`](#adddevice)
* [`checkDevice(...)`](#checkdevice)
* [`deleteDevice(...)`](#deletedevice)
* [`getDevice(...)`](#getdevice)
* [`createIncidenceFlow(...)`](#createincidenceflow)
* [`createIncidence(...)`](#createincidence)
* [`closeIncidence(...)`](#closeincidence)
* [`ecommerce(...)`](#ecommerce)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### initConfig(...)

```typescript
initConfig(options: { apikey: string; environment: string; }) => Promise<string>
```

| Param         | Type                                                  |
| ------------- | ----------------------------------------------------- |
| **`options`** | <code>{ apikey: string; environment: string; }</code> |

**Returns:** <code>Promise&lt;string&gt;</code>

--------------------


### addDevice(...)

```typescript
addDevice(options: { user: any; vehicle: any; }) => Promise<string>
```

| Param         | Type                                      |
| ------------- | ----------------------------------------- |
| **`options`** | <code>{ user: any; vehicle: any; }</code> |

**Returns:** <code>Promise&lt;string&gt;</code>

--------------------


### checkDevice(...)

```typescript
checkDevice(options: { user: any; vehicle: any; }) => Promise<string>
```

| Param         | Type                                      |
| ------------- | ----------------------------------------- |
| **`options`** | <code>{ user: any; vehicle: any; }</code> |

**Returns:** <code>Promise&lt;string&gt;</code>

--------------------


### deleteDevice(...)

```typescript
deleteDevice(options: { user: any; vehicle: any; }) => Promise<string>
```

| Param         | Type                                      |
| ------------- | ----------------------------------------- |
| **`options`** | <code>{ user: any; vehicle: any; }</code> |

**Returns:** <code>Promise&lt;string&gt;</code>

--------------------


### getDevice(...)

```typescript
getDevice(options: { user: any; vehicle: any; }) => Promise<string>
```

| Param         | Type                                      |
| ------------- | ----------------------------------------- |
| **`options`** | <code>{ user: any; vehicle: any; }</code> |

**Returns:** <code>Promise&lt;string&gt;</code>

--------------------


### createIncidenceFlow(...)

```typescript
createIncidenceFlow(options: { user: any; vehicle: any; }) => Promise<string>
```

| Param         | Type                                      |
| ------------- | ----------------------------------------- |
| **`options`** | <code>{ user: any; vehicle: any; }</code> |

**Returns:** <code>Promise&lt;string&gt;</code>

--------------------


### createIncidence(...)

```typescript
createIncidence(options: { user: any; vehicle: any; incidence: any; }) => Promise<string>
```

| Param         | Type                                                      |
| ------------- | --------------------------------------------------------- |
| **`options`** | <code>{ user: any; vehicle: any; incidence: any; }</code> |

**Returns:** <code>Promise&lt;string&gt;</code>

--------------------


### closeIncidence(...)

```typescript
closeIncidence(options: { user: any; vehicle: any; incidence: any; }) => Promise<string>
```

| Param         | Type                                                      |
| ------------- | --------------------------------------------------------- |
| **`options`** | <code>{ user: any; vehicle: any; incidence: any; }</code> |

**Returns:** <code>Promise&lt;string&gt;</code>

--------------------


### ecommerce(...)

```typescript
ecommerce(options: { user: any; vehicle: any; }) => Promise<string>
```

| Param         | Type                                      |
| ------------- | ----------------------------------------- |
| **`options`** | <code>{ user: any; vehicle: any; }</code> |

**Returns:** <code>Promise&lt;string&gt;</code>

--------------------

</docgen-api>
